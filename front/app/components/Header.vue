<template>
    <header>
        <div class="container">
            <div class="d-flex justify-content-between align-items-center p-3">
        <div class="logo">
          <nuxt-link to="/">
          <h1>ibira</h1>
        </nuxt-link>
        </div>
        <div class="profile">
          <nuxt-link v-if="!auth.isAuthenticated" to="/signin">
            <span>Entrar</span>
          </nuxt-link>
          <nuxt-link v-if="auth.isAuthenticated" to="/signin">
            <button class="btn-signout" @click="sigout()">Sair</button>
          </nuxt-link>
        </div>
      </div>
        </div>
      
    </header>
</template>
<script setup lang="ts"> 
import { onMounted } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useRouter } from 'vue-router';

const auth = useAuthStore();
const router = useRouter();

onMounted(() => {
  auth.checkAuth();
});

function sigout() {
  auth.logout();
  router.push('/');
}
</script>
<style scoped>
header {
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow);
    width: 100%;
    padding-top: 1rem;
    padding-bottom: 1rem;
}
.logo h1 {
  color: #19a311cf;
  font-weight: 700;
  font-family: 'Inter', sans-serif;
  font-size: 1.25rem;
}
.btn-signout {
  background-color: #fff;
  color: #464646;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}
a {
  text-decoration: none;
}
</style>