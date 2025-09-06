<script setup lang="ts">
import type { Link } from '~/types/Link';
const props = defineProps<{ link: Link }>()
</script>

<template>
  <li class="item mb-2">
    <h3> {{ props.link.title }}</h3>
    <a :href="props.link.url" target="_blank" class="text-green-600 font-semibold">
      {{ props.link.url }}
    </a>
    <p class="text-sm text-gray-600">{{ props.link.description }}</p>
  </li>
</template>
<style scoped>
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