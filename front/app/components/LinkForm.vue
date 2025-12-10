<script setup lang="ts">
import { linkStore } from '@/stores/linkStore'

const store = linkStore()

const form = reactive({ title: '', url: '', description: '' })
const sending = ref(false)

async function handleSubmit() {
  sending.value = true
  await store.addLink({ ...form })
  Object.assign(form, { title: '', url: '', description: '' })
  sending.value = false
}
</script>


<template>
  <div class="save-form">
    <h2>Salve um novo link</h2>
      <form @submit.prevent="handleSubmit" method="post">
        <div class="field">
          <label for="url">URL</label><br>
          <input v-model="form.url" type="text" name="url" id="url" placeholder="https://exemplo.com.br" required>
        </div>
        <div class="field">
          <label for="title">Título</label><br>
          <input v-model="form.title" type="text" name="title" id="titulo" placeholder="Dê um nome ao link" required>
        </div>
        <div class="field">
          <label for="description">Descrição</label><br>
          <textarea v-model="form.description" name="description" id="description" placeholder="Uma descrição curta"></textarea>
        </div>
        <div class="d-flex justify-content-center mb-3" v-if="sending">
            <LoadingSpinner/>
          </div>
        <button class="btn button-primary btn-save" :disabled="sending">Salvar</button>
      
      </form>
    </div>
</template>
<style scoped>
.save-form {
  background-color: #fff;
  border-radius: 0.75rem;;
  padding: 1.5rem;
}
.btn-save {
  width: 100%;
}
h2 {
  font-family: 'Inter', sans-serif;
  font-weight: 600;
  font-size: 1.25rem;
  margin-bottom: 1em;
}
.field input, textarea {
  background-color: #fff;
  border-color: #e5e7eb;
  border-width: 1px;
  width: 100%;
  padding: 0.5em;
  border-radius: 0.375rem;
  font-family: 'Inter', sans-serif;
  margin-top: 0.25em;
  margin-bottom: 1em;
  font-weight: 500;
  font-size: 0.875rem;
}
label {
  font-family: 'Inter', sans-serif;
  font-weight: 500;
  line-height: 1.25rem;
}
</style>