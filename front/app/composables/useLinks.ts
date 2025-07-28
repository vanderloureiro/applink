import { type Link } from '@/types/link'
import { defineStore } from 'pinia'

export const useLinks = defineStore('links', () => {
  const links = ref<Link[]>([])

  async function fetchLinks() {
    const res = await $fetch<Link[]>('http://localhost:8080/api/links')
    links.value = res
  }

  async function addLink(link: any) {
    await $fetch('http://localhost:8080/api/links', {
      method: 'POST',
      body: link,
      headers: {
        'api-key': link.key
      }
    })
    await fetchLinks() // <- força a atualização da lista após salvar
  }

  return { links, fetchLinks, addLink }
})