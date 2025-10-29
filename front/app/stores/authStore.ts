import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuthenticated: false,
  }),

  actions: {
    login(token: string) {
      localStorage.setItem('authToken', token);
      this.isAuthenticated = true;
    },

    logout() {
      localStorage.removeItem('authToken');
      this.isAuthenticated = false;
    },

    checkAuth() {
      const token = localStorage.getItem('authToken');
      this.isAuthenticated = !!token;
    }
  }
});
