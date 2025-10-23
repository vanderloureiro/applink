import { defineStore } from 'pinia';
import type { Link } from '~/types/Link';
import type { PaginatedResponse } from '~/types/PaginatedResponse';

export const linkStore = defineStore('links', {
  state: () => ({
    links: [] as Link[],
    pageNumber: 0,
    pageSize: 20,
    totalPages: 0,
    totalElements: 0,
    isEmpty: false
  }),

  actions: {
    async fetchLinks(query: string = '', page: number = 1, pageSize: number = 7) {
      const params = new URLSearchParams();
      if (query) params.set('query', query);
      params.set('page', (page - 1).toString());
      params.set('size', pageSize.toString());

      const url = `http://localhost:8080/api/links?${params}`;

      try {
        const res = await $fetch<PaginatedResponse>(url);
        this.links = res.content;
        this.pageNumber = res.pageNumber;
        this.pageSize = res.pageSize;
        this.totalPages = res.totalPage;
        this.totalElements = res.totalElements;
        this.isEmpty = res.empty;
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