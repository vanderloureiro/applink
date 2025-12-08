import { defineStore } from 'pinia';
import { linkStore } from './linkStore';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuthenticated: false,
  }),

  actions: {
    async login(token: string) {
      localStorage.setItem('authToken', token);
      this.isAuthenticated = true;
      
      // Fetch user data after login
      try {
        const { fetchUser } = useAuth();
        await fetchUser();
      } catch (error) {
        console.error('Failed to fetch user after login:', error);
      }
    },

    logout() {
      localStorage.removeItem('authToken');
      this.isAuthenticated = false;
      
      const links = linkStore();
      links.links = [];
      links.pageNumber = 0;
      links.pageSize = 20;
      links.totalPages = 0;
      links.totalElements = 0;
      links.isEmpty = false;
    },

    checkAuth() {
      const token = localStorage.getItem('authToken');
      this.isAuthenticated = !!token;
    }
  }
});
