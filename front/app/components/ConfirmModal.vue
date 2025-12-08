<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="close">
    <div class="confirm-modal">
      <h3>{{ title }}</h3>
      <p>{{ message }}</p>
      <div class="modal-actions">
        <button class="btn button-cancel" @click="close">Cancelar</button>
        <button class="btn button-danger" @click="confirm">Confirmar</button>
      </div>
    </div>
  </div>
</template>

<script setup>

defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: 'Confirmação',
  },
  message: {
    type: String,
    default: 'Tem certeza?',
  },
});

const emit = defineEmits(['confirm', 'close']);

function confirm() {
  emit('confirm');
}

function close() {
  emit('close');
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.confirm-modal {
  background-color: #fff;
  border-radius: 0.75rem;
  padding: 1.5rem;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h3 {
  font-family: 'Inter', sans-serif;
  font-weight: 600;
  font-size: 1.125rem;
  margin-bottom: 0.5em;
}

p {
  font-family: 'Inter', sans-serif;
  font-weight: 500;
  font-size: 0.875rem;
  color: #464646;
  margin-bottom: 1.5em;
}

.modal-actions {
  display: flex;
  gap: 0.75rem;
  justify-content: flex-end;
}

.btn {
  padding: 0.5em 1em;
  border: none;
  border-radius: 0.375rem;
  font-family: 'Inter', sans-serif;
  font-weight: 500;
  font-size: 0.875rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.button-cancel {
  background-color: #fff;
  color: #464646;
  border: 1px solid #e5e7eb;
}

.button-cancel:hover {
  background-color: #f3f4f6;
}

.button-danger {
  background-color: #dc3545;
  color: #fff;
}

.button-danger:hover {
  background-color: #c82333;
}
</style>
