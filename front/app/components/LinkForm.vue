<script setup lang="ts">
import { linkStore } from '@/stores/linkStore'

const store = linkStore()

const form = reactive({ title: '', url: '', description: '', key: '' })
const sending = ref(false)

async function handleSubmit() {
  sending.value = true
  await store.addLink({ ...form })
  Object.assign(form, { title: '', url: '', description: '', key: '' })
  sending.value = false
}
</script>


<template>
  <div class="save-form">
      <form @submit.prevent="handleSubmit" method="post">
        <div class="field">
          <label for="title">Título <small>*</small></label><br>
          <input v-model="form.title" type="text" name="title" id="titulo" required>
        </div>
        <div class="field">
          <label for="url">URL <small>*</small></label><br>
          <input v-model="form.url" type="text" name="url" id="url" required>
        </div>
        <div class="field">
          <label for="description">Descrição</label><br>
          <textarea v-model="form.description" name="description" id="description"></textarea>
        </div>
        <div class="field">
          <label for="key">Key *</label><br>
          <input v-model="form.key" type="password" name="key" id="key" required>
        </div>
        <div class="field btn-save">
          <button :disabled="sending">Salvar</button>
        </div>
      
      </form>
    </div>
</template>
<style scoped>
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
</style>