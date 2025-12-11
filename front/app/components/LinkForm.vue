<script setup lang="ts">
import { linkStore } from '@/stores/linkStore'

const store = linkStore()

const form = reactive({ title: '', url: '', description: '', shortenLink: false })
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
        <div class="field-checkbox mb-4">
          <input type="checkbox" name="shortenLink" v-model="form.shortenLink" />
          <label>Encurtar link</label>
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
.field-checkbox {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-family: 'Inter', sans-serif;
}

.field-checkbox input[type="checkbox"] {
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;

  width: 1.125rem;
  height: 1.125rem;
  border: 0.125rem solid #ccc;
  border-radius: 0.25rem;
  background-color: #fff;
  cursor: pointer;
  position: relative;
}

.field-checkbox input[type="checkbox"]:checked {
  background-color: #19a311cf;
  border-color: #19a311cf;
}

.field-checkbox input[type="checkbox"]:checked::after {
  content: "";
  position: absolute;
  top: 1px;
  left: 4px;
  width: 6px;
  height: 10px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}
label {
  font-family: 'Inter', sans-serif;
  font-weight: 500;
  line-height: 1.25rem;
}
</style>