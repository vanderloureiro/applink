# Página de perfil

## Projeto

Projeto Nuxt 4.0.0

## Funcionalidade - Página de perfil

Quando usuário logado, exibir o nome no cabeçalho e quando clicar, direcionar para outra página, sendo essa a página de perfil do usuário.


### Detalhes

- O header contido em app/components/Header.vue deve exibir o nome do usuário logado no botão com link
- os dados de usuário serão buscados em uma chamada REST GET para o path /api/users/me
- preencha o conteúdo na página app/pages/profile.vue
  - Os campos de nome e email são preenchidos automaticamente com os dados do usuário autenticado
  - O campo email é desabilitado (read-only)
- coloque no final da página a opção de "sair"
- o botão de "sair" deve fazer o comportamento atual de deslogar presente em app/components/Header.vue
- use os estilos visuais de CSS iguais aos presentes em app/pages/index.vue e os componentes usado por ele

### Implementação

- `useAuth.ts`: Composable que gerencia autenticação com tipagem TypeScript
- `profile.vue`: Página de perfil que carrega dados do usuário em `onMounted` e oferece logout