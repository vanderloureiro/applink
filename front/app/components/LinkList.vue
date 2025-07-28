<script setup lang="ts">
import { useLinks } from '@/composables/useLinks'
const { links } = useLinks()
</script>

<template>
  <div class="link-list">
    <p v-if="false">Carregando…</p>
    <!-- <p v-else-if="error">{{ error }}</p> -->
    <ul v-else>
      <LinkItem v-for="l in links" :key="l.id" :link="l" />
    </ul>
  </div>
</template>
<style scoped>
.link-list {
  margin: 0 15%;
}
</style>