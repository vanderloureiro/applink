<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  totalPages: {
    type: Number,
    required: true,
    default: 1,
  },
});

const emit = defineEmits(['page-change']);

const currentPage = ref(1);

watch(() => props.totalPages, (newTotal) => {
  if (currentPage.value > newTotal) {
    currentPage.value = newTotal;
  }
});

function changePage(page) {
  if (page >= 1 && page <= props.totalPages) {
    currentPage.value = page;
    emit('page-change', page);
  }
}
</script>
<template>
  <div class="pagination">
    <button
      :disabled="currentPage === 1"
      @click="changePage(currentPage - 1)"
      class="pagination-button"
    >
      Previous
    </button>
    <button
      v-for="page in props.totalPages"
      :key="page"
      :class="['pagination-button', { active: page === currentPage }]"
      @click="changePage(page)"
    >
      {{ page }}
    </button>
    <button
      :disabled="currentPage === props.totalPages"
      @click="changePage(currentPage + 1)"
      class="pagination-button"
    >
      Next
    </button>
  </div>
</template>

<style scoped>
.pagination {
  display: flex;
  gap: 10px;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}
.pagination-button {
  padding: 10px 15px;
  border: none;
  border-radius: 20px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  font-size: 14px;
}
.pagination-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
.pagination-button.active {
  background-color: #0056b3;
  font-weight: bold;
}
</style>
