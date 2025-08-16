<script setup>
</script>
<template>
  <div class="app">
    <header>
      <div class="header-body">
        <div class="logo">
          <h1>AppLink</h1>
        </div>
        <div class="profile">
          Entrar
        </div>
      </div>
    </header>

    <main class="p-4">
      <NuxtPage />
    </main>
  </div>
</template>
<style scoped>
.header-body {
  display: flex;
  justify-content: space-between;
  margin: 0 15%;
  align-items: center;
}
.header-body h1 {
  color: #19a311cf;
}
</style>
