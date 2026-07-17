# applink

Aplicação Go monolítica com frontend estático renderizado pelo próprio servidor.

## Estrutura

- `cmd/applink/main.go` - ponto de entrada da aplicação
- `internal/handler/handler.go` - servidor HTTP e roteamento
- `internal/handler/templates` - templates HTML embarcados
- `internal/handler/static` - CSS estático servido pelo Go

## Como executar

```bash
go run ./cmd/applink
```

Depois, acesse `http://localhost:8080`.
