<script setup>
import Header from './components/Header.vue';
import Footer from './components/Footer.vue';
import { setupAuthTokenMiddleware } from './middleware/authTokenMiddleware';

onMounted(() => {
  setupAuthTokenMiddleware(); // Initialize middleware
});
</script>
<template>
  <div class="app">
    <Header></Header>
    <div class="container app-content">
      <main>
        <NuxtPage />
      </main>
    </div>
    <Footer></Footer>
  </div>
</template>
<style scoped>
.app {
  height: 100%;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.app-content {
  flex: 1;
  padding-top: 1.25rem;
  padding-bottom: 1.25rem;
}
</style>
