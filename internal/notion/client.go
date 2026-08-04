package notion

import (
	"bytes"
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"net/http"
	"strings"
	"sync"
	"time"
)

const (
	defaultBaseURL = "https://api.notion.com/v1"
	apiVersion     = "2026-03-11"
)

type Link struct {
	ID          string
	Title       string
	URL         string
	Description string
	CreatedAt   time.Time
}

type NewLink struct {
	Title       string
	URL         string
	Description string
}

type Client struct {
	token      string
	databaseID string
	baseURL    string
	httpClient *http.Client

	mu         sync.Mutex
	dataSource *dataSource
}

type dataSource struct {
	ID         string                    `json:"id"`
	Properties map[string]propertySchema `json:"properties"`
}

type propertySchema struct {
	Name string `json:"name"`
	Type string `json:"type"`
}

func NewClient(token, databaseID string) (*Client, error) {
	return newClient(token, databaseID, defaultBaseURL, &http.Client{Timeout: 15 * time.Second})
}

func newClient(token, databaseID, baseURL string, httpClient *http.Client) (*Client, error) {
	if strings.TrimSpace(token) == "" {
		return nil, errors.New("NOTION_TOKEN is required")
	}
	if strings.TrimSpace(databaseID) == "" {
		return nil, errors.New("NOTION_DATABASE_ID is required")
	}

	return &Client{
		token:      token,
		databaseID: normalizeID(databaseID),
		baseURL:    strings.TrimRight(baseURL, "/"),
		httpClient: httpClient,
	}, nil
}

func (c *Client) ListLinks(ctx context.Context) ([]Link, error) {
	ds, err := c.resolveDataSource(ctx)
	if err != nil {
		return nil, err
	}

	createdAt, ok := findProperty(ds.Properties, "created_at")
	body := map[string]any{"page_size": 100}
	if ok {
		body["sorts"] = []map[string]string{{"property": createdAt.Name, "direction": "descending"}}
	}

	var response struct {
		Results []struct {
			ID          string                   `json:"id"`
			CreatedTime time.Time                `json:"created_time"`
			Properties  map[string]propertyValue `json:"properties"`
		} `json:"results"`
	}
	if err := c.request(ctx, http.MethodPost, "/data_sources/"+ds.ID+"/query", body, &response); err != nil {
		return nil, fmt.Errorf("query Notion links: %w", err)
	}

	links := make([]Link, 0, len(response.Results))
	for _, page := range response.Results {
		link := Link{ID: page.ID, CreatedAt: page.CreatedTime}
		if value, ok := findValue(page.Properties, "title"); ok {
			link.Title = plainText(value.Title)
		}
		if value, ok := findValue(page.Properties, "link"); ok {
			link.URL = value.URL
			if link.URL == "" {
				link.URL = plainText(value.RichText)
			}
		}
		if value, ok := findValue(page.Properties, "description"); ok {
			link.Description = plainText(value.RichText)
		}
		if value, ok := findValue(page.Properties, "created_at"); ok {
			switch value.Type {
			case "date":
				if value.Date != nil && value.Date.Start != "" {
					if parsed := parseTime(value.Date.Start); !parsed.IsZero() {
						link.CreatedAt = parsed
					}
				}
			case "created_time":
				if !value.CreatedTime.IsZero() {
					link.CreatedAt = value.CreatedTime
				}
			}
		}
		links = append(links, link)
	}
	return links, nil
}

func (c *Client) CreateLink(ctx context.Context, input NewLink) error {
	ds, err := c.resolveDataSource(ctx)
	if err != nil {
		return err
	}

	properties := make(map[string]any)
	if err := setTextProperty(properties, ds.Properties, "title", input.Title); err != nil {
		return err
	}
	if err := setTextProperty(properties, ds.Properties, "link", input.URL); err != nil {
		return err
	}
	if err := setTextProperty(properties, ds.Properties, "description", input.Description); err != nil {
		return err
	}
	if property, ok := findProperty(ds.Properties, "created_at"); ok && property.Type == "date" {
		properties[property.Name] = map[string]any{"date": map[string]string{"start": time.Now().UTC().Format(time.RFC3339)}}
	}

	payload := map[string]any{
		"parent":     map[string]string{"type": "data_source_id", "data_source_id": ds.ID},
		"properties": properties,
	}
	if err := c.request(ctx, http.MethodPost, "/pages", payload, nil); err != nil {
		return fmt.Errorf("create Notion link: %w", err)
	}
	return nil
}

func (c *Client) resolveDataSource(ctx context.Context) (*dataSource, error) {
	c.mu.Lock()
	defer c.mu.Unlock()
	if c.dataSource != nil {
		return c.dataSource, nil
	}

	var database struct {
		DataSources []struct {
			ID   string `json:"id"`
			Name string `json:"name"`
		} `json:"data_sources"`
	}
	if err := c.request(ctx, http.MethodGet, "/databases/"+c.databaseID, nil, &database); err != nil {
		return nil, fmt.Errorf("retrieve Notion database: %w", err)
	}
	if len(database.DataSources) == 0 {
		return nil, errors.New("Notion database has no data sources")
	}

	dataSourceID := database.DataSources[0].ID
	for _, candidate := range database.DataSources {
		if strings.EqualFold(candidate.Name, "link") {
			dataSourceID = candidate.ID
			break
		}
	}

	var ds dataSource
	if err := c.request(ctx, http.MethodGet, "/data_sources/"+dataSourceID, nil, &ds); err != nil {
		return nil, fmt.Errorf("retrieve Notion data source: %w", err)
	}
	for _, required := range []string{"title", "link", "description"} {
		if _, ok := findProperty(ds.Properties, required); !ok {
			return nil, fmt.Errorf("Notion data source is missing property %q", required)
		}
	}
	c.dataSource = &ds
	return c.dataSource, nil
}

func (c *Client) request(ctx context.Context, method, path string, payload, result any) error {
	var body io.Reader
	if payload != nil {
		encoded, err := json.Marshal(payload)
		if err != nil {
			return err
		}
		body = bytes.NewReader(encoded)
	}

	req, err := http.NewRequestWithContext(ctx, method, c.baseURL+path, body)
	if err != nil {
		return err
	}
	req.Header.Set("Authorization", "Bearer "+c.token)
	req.Header.Set("Notion-Version", apiVersion)
	req.Header.Set("Content-Type", "application/json")

	resp, err := c.httpClient.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()
	if resp.StatusCode < 200 || resp.StatusCode >= 300 {
		message, _ := io.ReadAll(io.LimitReader(resp.Body, 8<<10))
		return fmt.Errorf("Notion API returned %s: %s", resp.Status, strings.TrimSpace(string(message)))
	}
	if result == nil {
		return nil
	}
	return json.NewDecoder(resp.Body).Decode(result)
}

type propertyValue struct {
	Type        string     `json:"type"`
	Title       []richText `json:"title"`
	RichText    []richText `json:"rich_text"`
	URL         string     `json:"url"`
	CreatedTime time.Time  `json:"created_time"`
	Date        *struct {
		Start string `json:"start"`
	} `json:"date"`
}

type richText struct {
	PlainText string `json:"plain_text"`
}

func setTextProperty(target map[string]any, schema map[string]propertySchema, wanted, value string) error {
	property, ok := findProperty(schema, wanted)
	if !ok {
		return fmt.Errorf("Notion data source is missing property %q", wanted)
	}
	text := []map[string]any{{"type": "text", "text": map[string]string{"content": value}}}
	switch property.Type {
	case "title":
		target[property.Name] = map[string]any{"title": text}
	case "rich_text":
		target[property.Name] = map[string]any{"rich_text": text}
	case "url":
		target[property.Name] = map[string]string{"url": value}
	default:
		return fmt.Errorf("Notion property %q must be title, rich_text, or url; got %s", property.Name, property.Type)
	}
	return nil
}

func findProperty(properties map[string]propertySchema, wanted string) (propertySchema, bool) {
	for key, property := range properties {
		name := property.Name
		if name == "" {
			name = key
			property.Name = key
		}
		if strings.EqualFold(name, wanted) {
			return property, true
		}
	}
	return propertySchema{}, false
}

func findValue(properties map[string]propertyValue, wanted string) (propertyValue, bool) {
	for name, value := range properties {
		if strings.EqualFold(name, wanted) {
			return value, true
		}
	}
	return propertyValue{}, false
}

func plainText(items []richText) string {
	var builder strings.Builder
	for _, item := range items {
		builder.WriteString(item.PlainText)
	}
	return builder.String()
}

func parseTime(value string) time.Time {
	for _, layout := range []string{time.RFC3339, "2006-01-02"} {
		if parsed, err := time.Parse(layout, value); err == nil {
			return parsed
		}
	}
	return time.Time{}
}

func normalizeID(value string) string {
	return strings.ReplaceAll(strings.TrimSpace(value), "-", "")
}
