<script setup lang="ts">
import { linkStore } from '@/stores/linkStore';
import Pagination from '@/components/Pagination.vue';
import { ref, computed, onMounted } from 'vue';

const store = linkStore();
const searchQuery = ref('');

const links = computed(() => store.links);
const totalPages = computed(() => store.totalPages);

onMounted(() => {
  store.fetchLinks();
});

function handlePageChange(page: number) {
  store.fetchLinks(searchQuery.value, page);
}

function handleSearchInput(query: string) {
  searchQuery.value = query;
  store.fetchLinks(query);
}
</script>

<template>
  <div class="row">
    <div class="col-12 col-md-4 mt-3">
      <link-form></link-form>
    </div>
    <div class="col-12 col-md-8 mt-3">
      <input
        type="text"
        v-model="searchQuery"
        @input="handleSearchInput(searchQuery)"
        placeholder="Search links..."
        class="search-bar"
      />
      <link-list :links="links" />
      <Pagination 
        :totalPages="totalPages" 
        @page-change="handlePageChange" 
      />
    </div>
  </div>
</template>

<style scoped>
.search-bar {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 14px;
}

.row {
  margin: 0 auto;
  max-width: 1200px;
  padding: 0 1rem;
}

.col-12 {
  margin-bottom: 1.5rem;
}

@media (min-width: 768px) {
  .col-md-4 {
    padding-right: 1rem;
  }
  
  .col-md-8 {
    padding-left: 1rem;
  }
}
</style>

