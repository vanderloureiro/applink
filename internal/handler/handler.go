package handler

import (
	"embed"
	"html/template"
	"io/fs"
	"log"
	"net/http"
)

//go:embed templates/* static/*
var content embed.FS

func Run() error {
	tplFS, err := fs.Sub(content, "templates")
	if err != nil {
		return err
	}

	staticFS, err := fs.Sub(content, "static")
	if err != nil {
		return err
	}

	tpl, err := template.ParseFS(tplFS, "*.html")
	if err != nil {
		return err
	}

	mux := http.NewServeMux()
	mux.Handle("/static/", http.StripPrefix("/static/", http.FileServer(http.FS(staticFS))))
	mux.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		if r.URL.Path != "/" {
			http.NotFound(w, r)
			return
		}

		data := struct {
			Title string
		}{
			Title: "ibira",
		}

		if err := tpl.ExecuteTemplate(w, "index.html", data); err != nil {
			log.Printf("template execution error: %v", err)
			http.Error(w, "internal server error", http.StatusInternalServerError)
		}
	})

	log.Println("starting ibira on http://localhost:8080")
	return http.ListenAndServe(":8080", mux)
}
