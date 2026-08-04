# applink

Aplicação Go monolítica com frontend estático renderizado pelo próprio servidor.

## Estrutura

- `cmd/applink/main.go` - ponto de entrada da aplicação
- `internal/handler/handler.go` - servidor HTTP e roteamento
- `internal/handler/templates` - templates HTML embarcados
- `internal/handler/static` - CSS estático servido pelo Go

## Conexão com o Notion

A aplicação usa o database `link` (`3a06ced1da908091bbc0e2e95fdd0d65`) e espera as propriedades `title`, `link`, `description` e `created_at`. Os nomes não diferenciam maiúsculas de minúsculas.

1. Crie uma conexão interna em [Notion integrations](https://www.notion.so/profile/integrations).
2. Dê a ela as capacidades de leitura e inserção de conteúdo.
3. No database `link`, abra `•••` → `Connections` e adicione a conexão.
4. Crie um arquivo `.env` (você pode copiar `.env.example`) e informe o token:

```bash
NOTION_TOKEN="ntn_..."
```

A aplicação carrega `.env` automaticamente. Variáveis já exportadas pelo shell têm precedência. `NOTION_DATABASE_ID` é opcional e permite apontar para outro database; `ADDR` também é opcional e usa `:8080` por padrão.

## Como executar

```bash
go run ./cmd/applink
```

Depois, acesse `http://localhost:8080`.
