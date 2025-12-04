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
          <div v-if="auth.isAuthenticated" class="user-menu">
            <nuxt-link :to="`/profile`">
              <span class="user-name">olá, {{ user?.name }}</span>
            </nuxt-link>
          </div>
        </div>
      </div>
        </div>
      
    </header>
</template>
<script setup lang="ts"> 
import { onMounted } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useAuth } from '@/composables/useAuth';
import { useRouter } from 'vue-router';

const auth = useAuthStore();
const router = useRouter();
const { user, fetchUser } = useAuth();

onMounted(async () => {
  auth.checkAuth();
  if (auth.isAuthenticated) {
    await fetchUser();
  }
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
.user-menu {
  display: flex;
  align-items: center;
  gap: 16px;
}
.user-name {
  color: #464646;
  font-weight: 500;
  cursor: pointer;
}
.user-name:hover {
  color: #19a311cf;
}
.btn-signout {
  background-color: #fff;
  color: #464646;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}
.btn-signout:hover {
  background-color: #f0f0f0;
}
a {
  text-decoration: none;
}
</style>