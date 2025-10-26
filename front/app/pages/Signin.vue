<template>
  <div class="signin-page">
    <div v-if="!emailSent">
      <h2>Sign In</h2>
      <input
        type="email"
        v-model="email"
        placeholder="Enter your email"
        @keyup.enter="sendEmail"
      />
      <div class="accept-terms" v-if="isOnboardingUser">
        <input type="checkbox" name="acceptTerms" v-model="acceptTerms" />
        I accept the terms and conditions
      </div>
      <br />
      <button v-if="isOnboardingUser" @click="create">Create</button>
      <button v-if="!isOnboardingUser" @click="sendEmail">Send</button>
    </div>
    <div v-else>
      <h2>Enter Code</h2>
      <input
        type="text"
        v-model="code"
        placeholder="Enter 6-digit code"
        maxlength="6"
        @keyup.enter="verifyCode"
      />
      <button @click="verifyCode">Verify</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const email = ref('');
const code = ref('');
const acceptTerms = ref(false);
const isOnboardingUser = ref(false);
const emailSent = ref(false);
const router = useRouter();

const config = useRuntimeConfig();
const baseURL = config.public.apiBase || '/';

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
      } else if (response.status === 403) {
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
        const token = data.token;
        localStorage.setItem('authToken', token); // Save token in localStorage
        console.log('Code validated successfully and token saved:', token);
        router.push('/'); // Redirect to home page
      } else if (response.status === 403) {
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
  const token = localStorage.getItem('authToken');
  if (token) {
    router.push('/');
  }
});
</script>

<style scoped>
.signin-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 50px;
}
input {
  margin: 10px 0;
  padding: 10px;
  font-size: 16px;
  width: 300px;
}
button {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}
</style>
