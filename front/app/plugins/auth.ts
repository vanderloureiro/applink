import { setupAuthTokenMiddleware } from '../middleware/authTokenMiddleware'

export default defineNuxtPlugin(() => {
  setupAuthTokenMiddleware()
})
