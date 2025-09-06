<script setup lang="ts">
import { linkStore } from '@/stores/linkStore';
import Pagination from '@/components/Pagination.vue';

const store = linkStore();
const totalPages = 10; // Example dynamic value, can be fetched or computed

onMounted(() => {
  store.fetchLinks();
});

function handlePageChange(page: any) {
  console.log(`Page changed to: ${page}`);
}
</script>

<template>
  <div class="row">
    <div class="col-4 col-md-4">
      <link-form></link-form>
    </div>
    <div class="col-8 col-md-8">
      <link-list></link-list>
      <Pagination :totalPages="totalPages" @page-change="handlePageChange" />
    </div>
  </div>
</template>

