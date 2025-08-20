import { defineStore } from 'pinia'
import type { Link } from '~/types/Link'

export const linkStore = defineStore('links', {
  state: () => ({
    links: [] as Link[]
  }),

  actions: {
    async fetchLinks() {
      const res = await $fetch<Link[]>('http://localhost:8080/api/links')
      this.links = res
    },

    async addLink(link: any) {
      await $fetch('http://localhost:8080/api/links', {
        method: 'POST',
        body: link,
        headers: {
          'api-key': link.key
        }
      })
      await this.fetchLinks() // atualiza a lista
    }
  }
})