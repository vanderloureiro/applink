export default defineNuxtPlugin(() => {
  const originalFetch = window.fetch;

  window.fetch = async (input, init = {}) => {
    try {
      const token = localStorage.getItem('authToken');

      if (token != null) {
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
});
