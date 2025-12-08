# Excluir link

## Projeto

Projeto Nuxt 4.0.0

## Funcionalidade - Excluir link

Como um usuário, quero poder excluir um link salvo por mim

## Contexto

Na minha listagem de links, quero ter um botão em cada card de link com a opção de excluir, quando excluo, o link some da minha listagem

### Detalhes

- O botão será um ícone de "X" no canto superior direito de cada card de link
- Ao clicar no botão, um modal de confirmação deve aparecer
- A requisição para o backend só será feita após confirmação clicando no botão do modal
- A request será na estrutura do arquivo linkStore.ts, chamando um novo endpoint
- chamada do endpoint será
```
$fetch(`${baseURL}/api/links/{id}`, {
          method: 'DELETE',...
```
-- após deletado, o card deve sumir da listagem
-- o passo anterior pode ser feito fazendo a mesma consulta novamente e sobrepondo

### Implementação

- usar o arquivo linkStore.ts para funcionalidade
- o estilo do modal deve seguir o padrão do projeto como o contido em #profile.vue
- o card de link é o já existente #components/LinkItem.vue