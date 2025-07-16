import { resolve } from 'path'

// eslint-disable-next-line no-undef
export default defineNuxtConfig({
  ssr: false, // ou true se quiser SSR
  target: 'static',
  app: {
    baseURL: './' // caminhos relativos
  },
  nitro: {
    preset: 'static'
  }
})
