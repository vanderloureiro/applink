<script setup>
const items = ref([{ message: 'Foo' }, { message: 'Bar' }])
const contentTwo = ref(["Link1", "Link2", "Link3"])
</script>
<template>
  <header>
    <div class="header-body">
      <div class="logo">
        <h1>AppLink</h1>
      </div>
      <div class="profile">
        Entrar
      </div>
    </div>
  </header>
  <div class="content">
    <div class="save-form">
      <form action="#" method="post">
        <div class="field">
          <label for="title">Título <small>*</small></label><br>
          <input type="text" name="title" id="titulo" required>
        </div>
        <div class="field">
          <label for="link">URL <small>*</small></label><br>
          <input type="url" name="link" id="link" required>
        </div>
        <div class="field">
          <label for="description">Descrição</label><br>
          <textarea name="description" id="description"></textarea>
        </div>
        <div class="field">
          <label for="key">Key *</label><br>
          <input type="password" name="key" id="key" required>
        </div>
        <div class="field btn-save">
          <input type="button" value="Salvar">
        </div>
      
      </form>
    </div>
    <div class="link-list">
      <h4>Salvos</h4>
      <ul>
        <li v-for="linkbody in contentTwo">
          Link: {{ linkbody }}
        </li>
      </ul>
    </div>
  </div>
</template>
<style scoped>
.header-body {
  display: flex;
  justify-content: space-between;
  margin: 0 15%;
  align-items: center;
}
.save-form {
  margin: 5% 20%;
  background-color: rgb(149, 202, 149);
  border-radius: 15px;
  padding: 1%;
}
.field input, textarea {
  background-color: rgb(149, 202, 149);
}
.btn-save {
  display: flex;
  justify-content: end;
}
.link-list {
  margin: 0 15%;
}
</style>
