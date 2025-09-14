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
      <button @click="sendEmail">Send</button>
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
const emailSent = ref(false);
const router = useRouter();

async function sendEmail() {
  if (email.value) {
    try {
      const response = await fetch('http://localhost:8080/api/auth/sign-in', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: email.value }),
      });

      if (response.ok) {
        console.log('Email sent successfully:', email.value);
        emailSent.value = true;
      } else {
        console.error('Failed to send email. Status:', response.status);
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
      const response = await fetch('http://localhost:8080/api/auth/validate', {
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
