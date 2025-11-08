// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },
  css: [
    'bootstrap/dist/css/bootstrap.min.css',
    '~/assets/css/main.css'
  ],
  plugins: [
    '~/plugins/bootstrap.client.ts' 
  ],
  modules: ['@nuxt/eslint', '@nuxt/icon', '@pinia/nuxt'],
  runtimeConfig: {
    public: {
      apiBase: process.env.NUXT_PUBLIC_API_BASE || ''
    }
  },
  app: {
    baseURL: process.env.NUXT_PUBLIC_BASE_URL || '',
    cdnURL: process.env.NUXT_PUBLIC_BASE_URL || ''
  },
  nitro: {
    prerender: {
      crawlLinks: false,
      routes: ['/'],
      failOnError: false
    },
  },
})