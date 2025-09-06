<script setup>
import Footer from './components/Footer.vue';

</script>
<template>
  <div class="app">
    <div class="container">
      <header>
      <div class="d-flex justify-content-between align-items-center p-3">
        <div class="logo">
          <nuxt-link to="/">
          <h1>AppLink</h1>
        </nuxt-link>
        </div>
        <div class="profile">
          <nuxt-link to="/signin">
            <span>Entrar</span>
          </nuxt-link>
        </div>
      </div>
    </header>

    <main>
      <NuxtPage />
    </main>
    </div>
    <Footer></Footer>
  </div>
</template>
<style scoped>
.logo h1 {
  color: #19a311cf;
  font-weight: 700;
  font-family: 'Inter', sans-serif;
}
a {
  text-decoration: none;
}
</style>
