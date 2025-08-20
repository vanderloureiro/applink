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
import { ref } from 'vue';

const email = ref('');
const code = ref('');
const emailSent = ref(false);

function sendEmail() {
  if (email.value) {
    // Simulate sending email
    console.log(`Email sent to: ${email.value}`);
    emailSent.value = true;
  } else {
    alert('Please enter a valid email.');
  }
}

function verifyCode() {
  if (code.value.length === 6) {
    // Simulate verifying code
    console.log(`Code entered: ${code.value}`);
    alert('Code verified successfully!');
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
