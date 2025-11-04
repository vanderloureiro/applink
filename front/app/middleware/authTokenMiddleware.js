export function setupAuthTokenMiddleware() {
  const originalFetch = window.fetch;

  window.fetch = async (input, init = {}) => {
    try {
      const token = localStorage.getItem('authToken');
      
      if (token && (!init.method || init.method.toUpperCase() !== 'OPTIONS')) {
        init.headers = {
          ...init.headers,
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        };
      }
      return await originalFetch(input, init);
    } catch (error) {
      console.error('Fetch error:', error);
      throw error;
    }
  };
}
