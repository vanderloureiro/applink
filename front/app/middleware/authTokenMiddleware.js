export function setupAuthTokenMiddleware() {
  const originalFetch = window.fetch;

  window.fetch = async (input, init = {}) => {
    try {
      const token = localStorage.getItem('authToken');
      if (token && (!init.method || init.method.toUpperCase() !== 'OPTIONS')) {
        init.headers = {
          ...init.headers,
          authorization: `Bearer ${token}`,
        };
      }
      return await originalFetch(input, init);
    } catch (error) {
      console.error('Fetch error:', error);
      throw error; // Repropaga o erro para que ele seja tratado no chamador
    }
  };
}
