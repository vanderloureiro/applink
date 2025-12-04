import { ref } from 'vue';

interface UserResponse {
  id: string;
  name: string;
  email: string;
  isValidatedEmail: boolean;
  createdAt: string;
}

const user = ref<UserResponse | null>(null);

export const useAuth = () => {
    
  const fetchUser = async () => {
    const config = useRuntimeConfig();
    const baseURL = config.public.apiBase || '/';
    try {
      const url = `${baseURL}/api/users/me`;
      const response = await fetch(url);
      if (response.ok) {
        user.value = await response.json();
      }
    } catch (error) {
      console.error('Failed to fetch user:', error);
    }
  };

  const logout = async () => {
    // ...existing logout logic...
    user.value = null;
    navigateTo('/');
  };

  return {
    user,
    fetchUser,
    logout
  };
};
