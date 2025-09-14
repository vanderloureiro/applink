import { defineStore } from 'pinia'
import type { Link } from '~/types/Link'

export const linkStore = defineStore('links', {
  state: () => ({
    links: [] as Link[]
  }),

  actions: {
    async fetchLinks() {
      try {
        const res = await $fetch<Link[]>('http://localhost:8080/api/links');
        this.links = res;
      } catch (error) {
        console.error('Error fetching links:', error);
        throw error; // Repropaga o erro para que ele seja tratado no chamador
      }
    },

    async addLink(link: any) {
      try {
        await $fetch('http://localhost:8080/api/links', {
          method: 'POST',
          body: link,
          headers: {
            'api-key': link.key
          }
        });
        await this.fetchLinks(); // atualiza a lista
      } catch (error) {
        console.error('Error adding link:', error);
        throw error; // Repropaga o erro para que ele seja tratado no chamador
      }
    }
  }
})