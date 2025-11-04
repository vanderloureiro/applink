<template>
  <div class="row">
    <div class="col-12 col-md-6 offset-md-3 mt-3">
      <div class="signin-container">
        <div v-if="!emailSent">
          <div v-if="!isOnboardingUser">
            <h2>Sign In</h2>
          </div>
          <div v-if="isOnboardingUser">
            <h2>Create Account</h2>
          </div>
          <p v-if="isOnboardingUser" class="onboarding-alert">You don't have an account yet. It's easy to create one. Continue to create an account.</p>
          <div class="field">
            <input
              type="email"
              v-model="email"
              placeholder="Enter your email"
              @keyup.enter="sendEmail"
            />
          </div>
          <div class="field accept-terms" v-if="isOnboardingUser">
            <label>
              <input type="checkbox" name="acceptTerms" v-model="acceptTerms" />
              I accept the terms and conditions
            </label>
          </div>
          <button v-if="isOnboardingUser" class="btn button-primary btn-save" @click="create">Create</button>
          <button v-if="!isOnboardingUser" class="btn button-primary btn-save" @click="sendEmail">Send</button>
        </div>
        <div v-else>
          <h2>Authorization code</h2>
          <p class="onboarding-alert">You should have received a code in your email. Check it and enter it here.</p>
          
          <div class="field">
            <input
              type="text"
              v-model="code"
              placeholder="Enter 6-digit code"
              maxlength="6"
              @keyup.enter="verifyCode"
            />
          </div>
          <button class="btn button-primary btn-save" @click="verifyCode">Verify</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const email = ref('');
const code = ref('');
const acceptTerms = ref(false);
const isOnboardingUser = ref(false);
const emailSent = ref(false);
const router = useRouter();

const config = useRuntimeConfig();
const baseURL = config.public.apiBase || '/';

const auth = useAuthStore();

async function create() {
  if (acceptTerms.value) {
    try {
      const response = await fetch(`${baseURL}/api/users`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: email.value }),
      });

      if (response.ok) {
        console.log('User created successfully:', email.value);
        emailSent.value = true;
        isOnboardingUser.value = false;
        sendEmail(); 
      } else {
        console.log('Failed to create user. Status:', response.status);
      }
    } catch (error) {
      console.error('Error occurred while creating user:', error);
    }
  } else {
    alert('You must accept the terms and conditions to create an account.');
  }
}

async function sendEmail() {
  if (email.value) {
    try {
      const response = await fetch(`${baseURL}/api/auth/sign-in`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: email.value }),
      });

      if (response.ok) {
        console.log('Email sent successfully:', email.value);
        emailSent.value = true;
      } else if (response.status === 401) {
        console.log('Onboarding user.');
        isOnboardingUser.value = true;
      } else {
        console.log('Failed to send email. Status:', response.status);
      }
    } catch (error) {
      console.error('Error occurred while sending email:', error);
    }
  } else {
    alert('Please enter a valid email.');
  }
}

async function verifyCode() {
  if (code.value.length === 6) {
    try {
      const response = await fetch(`${baseURL}/api/auth/validate`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: email.value, code: code.value }),
      });

      if (response.ok) {
        const data = await response.json();
        auth.login(data.token); // Usar a store para fazer login
        router.push('/');
      } else if (response.status === 401) {
        console.error('Onboarding user.');
        isOnboardingUser.value = true;
      } else {
        console.error('Failed to validate code. Status:', response.status);
      }
    } catch (error) {
      console.error('Error occurred while validating code:', error);
    }
  } else {
    alert('Please enter a valid 6-digit code.');
  }
}

onMounted(() => {
  auth.checkAuth();
  if (auth.isAuthenticated) {
    router.push('/');
  }
});
</script>

<style scoped>
.row {
  margin: 0 auto;
  max-width: 1200px;
  padding: 0 1rem;
}

.signin-container {
  background-color: #fff;
  border-radius: 0.75rem;
  padding: 1.5rem;
}

h2 {
  font-family: 'Inter', sans-serif;
  font-weight: 600;
  font-size: 1.25rem;
  margin-bottom: 1.5rem;
  color: #111827;
}

.onboarding-alert {
  font-family: 'Inter', sans-serif;
  font-size: 0.875rem;
  color: #754c4c;
  margin-bottom: 1rem;
}

.field {
  margin-bottom: 1rem;
}

.field input {
  background-color: #fff;
  border: 1px solid #e5e7eb;
  width: 100%;
  padding: 0.625rem;
  border-radius: 0.375rem;
  font-family: 'Inter', sans-serif;
  font-weight: 400;
  font-size: 0.875rem;
  transition: border-color 0.2s;
}

.field input:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.1);
}

.accept-terms {
  font-family: 'Inter', sans-serif;
  font-size: 0.875rem;
  color: #374151;
}

.accept-terms input[type="checkbox"] {
  width: auto;
  margin-right: 0.5rem;
}

.btn-save {
  width: 100%;
  color: white;
  padding: 0.625rem;
  border-radius: 0.375rem;
  font-weight: 500;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
  font-family: 'Inter', sans-serif;
}

@media (min-width: 768px) {
  .offset-md-3 {
    margin-left: 25%;
  }
  
  .col-md-6 {
    width: 50%;
  }
}
</style>
