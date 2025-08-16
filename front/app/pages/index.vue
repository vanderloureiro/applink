<script setup lang="ts">
import { linkStore } from '@/stores/linkStore'

const store = linkStore()

onMounted(() => {
  store.fetchLinks()
})
</script>

<template>
  <link-form></link-form>
  <section>
    <link-list></link-list>
  </section>
</template>