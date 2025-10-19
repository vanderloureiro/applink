export function setupAuthTokenMiddleware() {
  const originalFetch = window.fetch;

  window.fetch = async (input, init = {}) => {
    try {
      const token = localStorage.getItem('authToken');

      const headers = new Headers(init.headers || {});

      if (token && (!init.method || init.method.toUpperCase() !== 'OPTIONS')) {
        headers.set('Authorization', `Bearer ${token}`);
        headers.set('Content-Type', 'application/json');
      }
      console.log('Request Headers:', Object.fromEntries(headers.entries()));
      return await originalFetch(input, init);
    } catch (error) {
      console.error('Fetch error:', error);
      throw error;
    }
  };
}
