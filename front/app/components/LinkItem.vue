<script setup lang="ts">
import type { Link } from '@/types/link';
const props = defineProps<{ link: Link }>()
</script>

<template>
  <li class="rounded-xl p-3 shadow bg-white mb-2">
    <a :href="props.link.url" target="_blank" class="text-green-600 font-semibold">
      {{ props.link.title }}
    </a>
    <p class="text-sm text-gray-600">{{ props.link.description }}</p>
    <hr>
  </li>
</template>