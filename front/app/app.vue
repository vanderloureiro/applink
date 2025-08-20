<script setup>
import Footer from './components/Footer.vue';

</script>
<template>
  <div class="app">
    <header>
      <div class="header-body">
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
    <Footer></Footer>
  </div>
</template>
<style scoped>
.app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.header-body {
  display: flex;
  justify-content: space-between;
  margin: 0 15%;
  align-items: center;
}
.header-body h1 {
  color: #19a311cf;
}
main {
  flex: 1;
}
a {
  text-decoration: none;
}
</style>
