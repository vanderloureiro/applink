export function setupAuthTokenMiddleware() {
  if (typeof window === 'undefined') {
    // Não execute no lado do servidor
    return;
  }
  
  const originalFetch = window.fetch;

  window.fetch = async (input, init = {}) => {
    try {
      const token = localStorage.getItem('authToken');
      
      if (token) {
        init.headers = {
          ...init.headers,
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        };
      }

      const response = await originalFetch(input, init);
      
      if (response.status === 403) {
        localStorage.removeItem('authToken');
      }
      
      return response;
    } catch (error) {
      console.error('Fetch error:', error);
      throw error;
    }
  };
}
