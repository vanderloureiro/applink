<script setup>
const items = ref([{ message: 'Foo' }, { message: 'Bar' }])
const contentTwo = ref(["Link1", "Link2", "Link3"])

const title = ref('')
const url = ref('')
const description = ref('')
const key = ref('')

async function save() {

  try {
    await $fetch('http://localhost:8080/links', {
      method: 'POST',
      body: {
        title: title.value,
        url: url.value,
        description: description.value
      },
      headers: {
        'api-key': key.value
      }
    })
  } catch (error) {
    console.log(error)
  }
  title.value = ''
  url.value = ''
  description.value = ''
  key.value = ''
}
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
      <form @submit.prevent="save" method="post">
        <div class="field">
          <label for="title">Título <small>*</small></label><br>
          <input v-model="title" type="text" name="title" id="titulo" required>
        </div>
        <div class="field">
          <label for="url">URL <small>*</small></label><br>
          <input v-model="url" type="text" name="url" id="url" required>
        </div>
        <div class="field">
          <label for="description">Descrição</label><br>
          <textarea v-model="description" name="description" id="description"></textarea>
        </div>
        <div class="field">
          <label for="key">Key *</label><br>
          <input v-model="key" type="password" name="key" id="key" required>
        </div>
        <div class="field btn-save">
          <button>Salvar</button>
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
