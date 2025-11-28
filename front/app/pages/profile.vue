<template>
  <div class="profile-page">
    <h1>Profile Settings</h1>
    <form @submit.prevent="updateProfile">
      <div class="form-group">
        <label for="name">Name</label>
        <input
          id="name"
          type="text"
          v-model="name"
          placeholder="Enter your name"
        />
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input
          id="email"
          type="email"
          v-model="email"
          placeholder="Enter your email"
          disabled
        />
      </div>
      <div class="form-group">
        <label for="notifications">Notifications</label>
        <select id="notifications" v-model="notifications">
          <option value="enabled">Enabled</option>
          <option value="disabled">Disabled</option>
        </select>
      </div>
      <button type="submit">Save Changes</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const name = ref('John Doe'); // Example default value
const email = ref('johndoe@example.com'); // Example default value
const notifications = ref('enabled'); // Example default value

function updateProfile() {
  console.log('Profile updated:', {
    name: name.value,
    email: email.value,
    notifications: notifications.value,
  });
  alert('Profile updated successfully!');
}
</script>

<style scoped>
.profile-page {
  max-width: 600px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}
h1 {
  text-align: center;
  margin-bottom: 20px;
}
.form-group {
  margin-bottom: 15px;
}
label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
input,
select {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  display: block;
  width: 100%;
  padding: 10px;
  font-size: 16px;
  color: white;
  background-color: #007bff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background-color: #0056b3;
}
</style>