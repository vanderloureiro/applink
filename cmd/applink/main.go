package main

import (
	"log"

	"github.com/vanderloureiro/applink/internal/handler"
)

func main() {
	if err := handler.Run(); err != nil {
		log.Fatalf("server failed: %v", err)
	}
}
