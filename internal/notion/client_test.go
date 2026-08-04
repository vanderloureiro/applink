package notion

import (
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestClientListsAndCreatesLinks(t *testing.T) {
	t.Helper()
	var databaseRequests int
	var created map[string]any

	handler := http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if got := r.Header.Get("Authorization"); got != "Bearer secret" {
			t.Errorf("Authorization = %q", got)
		}
		if got := r.Header.Get("Notion-Version"); got != apiVersion {
			t.Errorf("Notion-Version = %q", got)
		}

		switch r.Method + " " + r.URL.Path {
		case "GET /databases/3a06ced1da908091bbc0e2e95fdd0d65":
			databaseRequests++
			writeJSON(t, w, map[string]any{"data_sources": []map[string]string{{"id": "source-id", "name": "link"}}})
		case "GET /data_sources/source-id":
			writeJSON(t, w, map[string]any{
				"id": "source-id",
				"properties": map[string]any{
					"title":       map[string]string{"name": "title", "type": "title"},
					"link":        map[string]string{"name": "link", "type": "url"},
					"description": map[string]string{"name": "description", "type": "rich_text"},
					"created_at":  map[string]string{"name": "created_at", "type": "created_time"},
				},
			})
		case "POST /data_sources/source-id/query":
			writeJSON(t, w, map[string]any{"results": []map[string]any{{
				"id": "page-id", "created_time": "2026-08-03T12:00:00Z",
				"properties": map[string]any{
					"title":       map[string]any{"type": "title", "title": []map[string]string{{"plain_text": "Go"}}},
					"link":        map[string]any{"type": "url", "url": "https://go.dev"},
					"description": map[string]any{"type": "rich_text", "rich_text": []map[string]string{{"plain_text": "Go docs"}}},
					"created_at":  map[string]any{"type": "created_time", "created_time": "2026-08-03T12:00:00Z"},
				},
			}}})
		case "POST /pages":
			if err := json.NewDecoder(r.Body).Decode(&created); err != nil {
				t.Fatalf("decode create payload: %v", err)
			}
			writeJSON(t, w, map[string]string{"id": "new-page"})
		default:
			http.NotFound(w, r)
		}
	})
	httpClient := &http.Client{Transport: roundTripFunc(func(r *http.Request) (*http.Response, error) {
		response := httptest.NewRecorder()
		handler.ServeHTTP(response, r)
		return response.Result(), nil
	})}

	client, err := newClient("secret", "3a06-ced1-da90-8091-bbc0-e2e95fdd0d65", "https://notion.test", httpClient)
	if err != nil {
		t.Fatal(err)
	}
	links, err := client.ListLinks(context.Background())
	if err != nil {
		t.Fatal(err)
	}
	if len(links) != 1 || links[0].Title != "Go" || links[0].URL != "https://go.dev" || links[0].Description != "Go docs" {
		t.Fatalf("unexpected links: %#v", links)
	}

	err = client.CreateLink(context.Background(), NewLink{Title: "Notion", URL: "https://notion.so", Description: "Workspace"})
	if err != nil {
		t.Fatal(err)
	}
	if databaseRequests != 1 {
		t.Fatalf("database requests = %d, want cached resolution", databaseRequests)
	}
	properties := created["properties"].(map[string]any)
	if properties["created_at"] != nil {
		t.Fatal("created_time property must not be sent when creating a page")
	}
	if got := properties["link"].(map[string]any)["url"]; got != "https://notion.so" {
		t.Fatalf("created link = %v", got)
	}
}

type roundTripFunc func(*http.Request) (*http.Response, error)

func (fn roundTripFunc) RoundTrip(request *http.Request) (*http.Response, error) {
	return fn(request)
}

func writeJSON(t *testing.T, w http.ResponseWriter, value any) {
	t.Helper()
	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(value); err != nil {
		t.Fatal(err)
	}
}
