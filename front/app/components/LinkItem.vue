<script setup lang="ts">
import type { Link } from '~/types/Link';
import { ref } from 'vue';
import { linkStore } from '@/stores/linkStore';
import ConfirmModal from './ConfirmModal.vue';

const props = defineProps<{ link: Link }>();

const store = linkStore();
const showModal = ref(false);

function openModal() {
  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
}

async function handleDelete() {
  try {
    await store.deleteLink(props.link.id);
    closeModal();
  } catch (error) {
    console.error('Failed to delete link:', error);
    alert('Erro ao excluir link');
  }
}
</script>

<template>
  <div class="link-item">
    <button class="delete-btn" @click="openModal">✕</button>
    <li class="item mb-2">
      <h3> {{ props.link.title }}</h3>
      <a :href="props.link.url" target="_blank" class="text-green-600 font-semibold">
        {{ props.link.url }}
      </a>
      <p class="text-sm text-gray-600">{{ props.link.description }}</p>
    </li>
  </div>
  <ConfirmModal 
    :isOpen="showModal" 
    title="Excluir link"
    message="Tem certeza que deseja excluir este link?"
    @confirm="handleDelete"
    @close="closeModal"
  />
</template>

<style scoped>
.link-item {
  position: relative;
}

.delete-btn {
  position: absolute;
  top: 0.75rem;
  right: 0.75rem;
  background-color: #fff;
  color: #292727;
  border: none;
  border-radius: 50%;
  width: 2rem;
  height: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 1.25rem;
  transition: background-color 0.2s;
  padding: 0;
}

.delete-btn:hover {
  color: #c82333;
}

.item {
  border-radius: 0.75rem;
  --tw-bg-opacity: 1;
  background-color: rgb(255 255 255 / var(--tw-bg-opacity, 1));
  padding: 1.5rem;
  --tw-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05);
  --tw-shadow-colored: 0 1px 2px 0 var(--tw-shadow-color);
  box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow);
  transition-property: box-shadow;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 300ms;
}
li {
  background-color: #fff;
  border-radius: 0.75rem;
  padding: 1.5rem;
  margin-bottom: 1em;
  list-style-type: none;
}
h3 {
  font-family: 'Inter', sans-serif;
  font-weight: 600;
  font-size: 1.125rem;
  margin-bottom: 0.25em;
}
a {
  color: #17cf17;
}
</style>