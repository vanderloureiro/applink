<script setup lang="ts">
import type { Link } from '~/types/Link';

const props = defineProps({
  links: {
    type: Array as () => Link[],
    required: true,
  },
});
</script>

<template>
  <div class="link-list">
    <p v-if="false">Carregando…</p>
    <!-- <p v-else-if="error">{{ error }}</p> -->
    <ul v-else>
      <LinkItem v-for="link in links" :key="link.id" :link="link" />
    </ul>
  </div>
</template>

<style scoped>
.link-list ul {
  padding-inline-start: 0;
}
</style>