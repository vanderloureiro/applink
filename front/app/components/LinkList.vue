<script setup lang="ts">
import { linkStore } from '@/stores/linkStore'

const store = linkStore()
onMounted(() => {
  store.fetchLinks()
})
</script>

<template>
  <div class="link-list">
    <p v-if="false">Carregando…</p>
    <!-- <p v-else-if="error">{{ error }}</p> -->
    <ul v-else>
      <LinkItem v-for="l in store.links" :key="l.id" :link="l" />
    </ul>
  </div>
</template>
<style scoped>
ul {
  padding-inline-start: 0;
}
</style>