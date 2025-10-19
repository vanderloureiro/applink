import { defineStore } from 'pinia';
import type { Link } from '~/types/Link';

export const linkStore = defineStore('links', {
  state: () => ({
    links: [] as Link[],
  }),

  actions: {
    async fetchLinks(query: string = '', page: number = 1, pageSize: number = 10) {
      const params = new URLSearchParams();
      if (query) params.set('query', query);
      params.set('page', (page - 1).toString()); // Ajusta para base 0
      params.set('size', pageSize.toString());

      const url = `http://localhost:8080/api/links?${params}`; // Garante que os parâmetros sejam anexados

      try {
        const res = await $fetch<Link[]>(url);
        this.links = res;
      } catch (error) {
        console.error('Error fetching links:', error);
        throw error;
      }
    },

    async addLink(link: any) {
      try {
        await $fetch('http://localhost:8080/api/links', {
          method: 'POST',
          body: link,
        });
        await this.fetchLinks();
      } catch (error) {
        console.error('Error adding link:', error);
        throw error;
      }
    },
  },
});