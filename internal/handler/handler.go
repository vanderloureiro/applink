package handler

import (
	"context"
	"embed"
	"fmt"
	"html/template"
	"io/fs"
	"log"
	"net/http"
	"net/url"
	"os"
	"strings"
	"time"

	"github.com/vanderloureiro/applink/internal/notion"
)

//go:embed templates/* static/*
var content embed.FS

type LinkStore interface {
	ListLinks(context.Context) ([]notion.Link, error)
	CreateLink(context.Context, notion.NewLink) error
}

type Server struct {
	store LinkStore
	tpl   *template.Template
}

type pageData struct {
	Title string
	Links []notion.Link
	Error string
	Form  notion.NewLink
}

func Run() error {
	if err := loadEnvFile(".env"); err != nil {
		return err
	}
	client, err := notion.NewClient(os.Getenv("NOTION_TOKEN"), envOrDefault("NOTION_DATABASE_ID", "3a06ced1da908091bbc0e2e95fdd0d65"))
	if err != nil {
		return err
	}

	server, err := NewServer(client)
	if err != nil {
		return err
	}

	address := envOrDefault("ADDR", ":8080")
	log.Printf("starting applink on http://localhost%s", address)
	return http.ListenAndServe(address, server.Routes())
}

func loadEnvFile(path string) error {
	contents, err := os.ReadFile(path)
	if err != nil {
		if os.IsNotExist(err) {
			return nil
		}
		return err
	}
	for lineNumber, rawLine := range strings.Split(string(contents), "\n") {
		line := strings.TrimSpace(rawLine)
		if line == "" || strings.HasPrefix(line, "#") {
			continue
		}
		line = strings.TrimSpace(strings.TrimPrefix(line, "export "))
		key, value, ok := strings.Cut(line, "=")
		key = strings.TrimSpace(key)
		if !ok || key == "" {
			return fmt.Errorf("invalid .env entry on line %d", lineNumber+1)
		}
		if _, exists := os.LookupEnv(key); exists {
			continue
		}
		value = strings.TrimSpace(value)
		if len(value) >= 2 && ((value[0] == '"' && value[len(value)-1] == '"') || (value[0] == '\'' && value[len(value)-1] == '\'')) {
			value = value[1 : len(value)-1]
		}
		if err := os.Setenv(key, value); err != nil {
			return fmt.Errorf("set environment variable %s: %w", key, err)
		}
	}
	return nil
}

func NewServer(store LinkStore) (*Server, error) {
	tplFS, err := fs.Sub(content, "templates")
	if err != nil {
		return nil, err
	}
	tpl, err := template.New("index.html").Funcs(template.FuncMap{
		"formatDate": func(value time.Time) string {
			if value.IsZero() {
				return ""
			}
			return value.Local().Format("02 Jan 2006")
		},
	}).ParseFS(tplFS, "*.html")
	if err != nil {
		return nil, err
	}
	return &Server{store: store, tpl: tpl}, nil
}

func (s *Server) Routes() http.Handler {
	staticFS, err := fs.Sub(content, "static")
	if err != nil {
		panic(err)
	}

	mux := http.NewServeMux()
	mux.Handle("/static/", http.StripPrefix("/static/", http.FileServer(http.FS(staticFS))))
	mux.HandleFunc("/", s.home)
	mux.HandleFunc("POST /links", s.createLink)
	return mux
}

func (s *Server) home(w http.ResponseWriter, r *http.Request) {
	if r.URL.Path != "/" {
		http.NotFound(w, r)
		return
	}
	links, err := s.store.ListLinks(r.Context())
	data := pageData{Title: "applink", Links: links}
	if err != nil {
		log.Printf("list Notion links: %v", err)
		data.Error = "Could not load links from Notion. Check the connection and try again."
	}
	s.render(w, http.StatusOK, data)
}

func (s *Server) createLink(w http.ResponseWriter, r *http.Request) {
	r.Body = http.MaxBytesReader(w, r.Body, 64<<10)
	if err := r.ParseForm(); err != nil {
		s.renderCreateError(w, r, notion.NewLink{}, "Invalid form submission.")
		return
	}

	input := notion.NewLink{
		Title:       strings.TrimSpace(r.FormValue("title")),
		URL:         strings.TrimSpace(r.FormValue("link")),
		Description: strings.TrimSpace(r.FormValue("description")),
	}
	if input.Title == "" || input.URL == "" {
		s.renderCreateError(w, r, input, "Title and link are required.")
		return
	}
	parsed, err := url.ParseRequestURI(input.URL)
	if err != nil || (parsed.Scheme != "http" && parsed.Scheme != "https") || parsed.Host == "" {
		s.renderCreateError(w, r, input, "Enter a valid http:// or https:// link.")
		return
	}
	if err := s.store.CreateLink(r.Context(), input); err != nil {
		log.Printf("create Notion link: %v", err)
		s.renderCreateError(w, r, input, "Could not save the link to Notion. Try again.")
		return
	}
	http.Redirect(w, r, "/", http.StatusSeeOther)
}

func (s *Server) renderCreateError(w http.ResponseWriter, r *http.Request, input notion.NewLink, message string) {
	links, err := s.store.ListLinks(r.Context())
	if err != nil {
		log.Printf("list Notion links after form error: %v", err)
	}
	s.render(w, http.StatusUnprocessableEntity, pageData{Title: "applink", Links: links, Error: message, Form: input})
}

func (s *Server) render(w http.ResponseWriter, status int, data pageData) {
	w.Header().Set("Content-Type", "text/html; charset=utf-8")
	w.WriteHeader(status)
	if err := s.tpl.ExecuteTemplate(w, "index.html", data); err != nil {
		log.Printf("template execution error: %v", err)
	}
}

func envOrDefault(key, fallback string) string {
	if value := strings.TrimSpace(os.Getenv(key)); value != "" {
		return value
	}
	return fallback
}
