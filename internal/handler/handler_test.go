package handler

import (
	"context"
	"net/http"
	"net/http/httptest"
	"net/url"
	"strings"
	"testing"

	"github.com/vanderloureiro/applink/internal/notion"
)

type fakeStore struct {
	links   []notion.Link
	created []notion.NewLink
}

func (f *fakeStore) ListLinks(context.Context) ([]notion.Link, error) {
	return f.links, nil
}

func (f *fakeStore) CreateLink(_ context.Context, link notion.NewLink) error {
	f.created = append(f.created, link)
	return nil
}

func TestHomeRendersStoredLinks(t *testing.T) {
	store := &fakeStore{links: []notion.Link{{Title: "Go", URL: "https://go.dev", Description: "Docs"}}}
	server, err := NewServer(store)
	if err != nil {
		t.Fatal(err)
	}

	response := httptest.NewRecorder()
	server.Routes().ServeHTTP(response, httptest.NewRequest(http.MethodGet, "/", nil))
	if response.Code != http.StatusOK {
		t.Fatalf("status = %d", response.Code)
	}
	for _, expected := range []string{"1 links", "Go", "https://go.dev", "Docs"} {
		if !strings.Contains(response.Body.String(), expected) {
			t.Errorf("body does not contain %q", expected)
		}
	}
}

func TestCreateLink(t *testing.T) {
	store := &fakeStore{}
	server, err := NewServer(store)
	if err != nil {
		t.Fatal(err)
	}
	form := url.Values{"title": {" Notion "}, "link": {"https://notion.so"}, "description": {" Workspace "}}
	request := httptest.NewRequest(http.MethodPost, "/links", strings.NewReader(form.Encode()))
	request.Header.Set("Content-Type", "application/x-www-form-urlencoded")
	response := httptest.NewRecorder()

	server.Routes().ServeHTTP(response, request)
	if response.Code != http.StatusSeeOther {
		t.Fatalf("status = %d, body = %s", response.Code, response.Body.String())
	}
	if len(store.created) != 1 || store.created[0].Title != "Notion" || store.created[0].Description != "Workspace" {
		t.Fatalf("created = %#v", store.created)
	}
}

func TestCreateLinkRejectsInvalidURL(t *testing.T) {
	store := &fakeStore{}
	server, err := NewServer(store)
	if err != nil {
		t.Fatal(err)
	}
	form := url.Values{"title": {"Bad"}, "link": {"javascript:alert(1)"}}
	request := httptest.NewRequest(http.MethodPost, "/links", strings.NewReader(form.Encode()))
	request.Header.Set("Content-Type", "application/x-www-form-urlencoded")
	response := httptest.NewRecorder()

	server.Routes().ServeHTTP(response, request)
	if response.Code != http.StatusUnprocessableEntity {
		t.Fatalf("status = %d", response.Code)
	}
	if len(store.created) != 0 {
		t.Fatal("invalid link was created")
	}
}
