<template>
    <div
      class="spinner"
      :style="{
        width: size + 'px',
        height: size + 'px',
        borderWidth: stroke + 'px'
      }"
    />
  </template>
  
  <script setup>
  const props = defineProps({
    size: { type: Number, default: 32 },   // tamanho do círculo
    stroke: { type: Number, default: 4 }   // espessura da borda
  })
  </script>
  
  <style scoped>
  .spinner {
    border-radius: 50%;
    border-style: solid;
    border-color: #ccc;
    border-top-color: #000;
    animation: spin 0.8s linear infinite;
  }
  
  @keyframes spin {
    to {
      transform: rotate(360deg);
    }
  }
  </style>