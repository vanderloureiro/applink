// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },
  css: [
    'bootstrap/dist/css/bootstrap.min.css',
    '~/assets/css/main.css'
  ],
  modules: ['@nuxt/eslint', '@nuxt/icon', '@pinia/nuxt', '@bootstrap-vue-next/nuxt'],
  runtimeConfig: {
    public: {
      apiBase: process.env.NUXT_PUBLIC_API_BASE || 'http://localhost:8080'
    }
  }
})