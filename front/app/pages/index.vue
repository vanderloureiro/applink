<script setup lang="ts">
const linkStore = useLinks()

onMounted(() => {
  linkStore.fetchLinks()
})
</script>

<template>
  <link-form></link-form>
  <section>
    <link-list></link-list>
  </section>
</template>