<template>
  <div class="row">
    <div class="col-12 col-md-6 offset-md-3 mt-3">
      <div class="profile-page">
        <h2>Profile Settings</h2>
        <form @submit.prevent="updateProfile">
          <div class="field">
            <label for="name">Name</label>
            <input
              id="name"
              type="text"
              v-model="name"
              placeholder="Enter your name" disabled
            />
          </div>
          <div class="field">
            <label for="email">Email</label>
            <input
              id="email"
              type="email"
              v-model="email"
              placeholder="Enter your email"
              disabled
            />
          </div>
          <!-- <div class="field">
            <label for="notifications">Notifications</label>
            <select id="notifications" v-model="notifications">
              <option value="enabled">Enabled</option>
              <option value="disabled">Disabled</option>
            </select>
          </div> -->
          <!-- <button type="submit" class="btn button-primary">Save Changes</button> -->
        </form>
        <button class="btn button-danger" @click="handleLogout">Sign Out</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuth } from '~/composables/useAuth';

const { user, fetchUser, logout } = useAuth();

const name = ref('');
const email = ref('');
const notifications = ref('enabled');

onMounted(async () => {
  await fetchUser();
  if (user.value) {
    name.value = user.value.name;
    email.value = user.value.email;
  }
});

function updateProfile() {
  console.log('Profile updated:', {
    name: name.value,
    email: email.value,
    notifications: notifications.value,
  });
  alert('Profile updated successfully!');
}

function handleLogout() {
  logout();
}
</script>

<style scoped>
.row {
  margin: 0 auto;
  max-width: 1200px;
  padding: 0 1rem;
}

.col-12 {
  margin-bottom: 1.5rem;
}

@media (min-width: 768px) {
  .offset-md-3 {
    margin-left: 25%;
  }
  
  .col-md-6 {
    flex: 0 0 50%;
  }
}

.profile-page {
  background-color: #fff;
  border-radius: 0.75rem;
  padding: 1.5rem;
}

h2 {
  font-family: 'Inter', sans-serif;
  font-weight: 600;
  font-size: 1.25rem;
  margin-bottom: 1em;
}

.field {
  margin-bottom: 1em;
}

.field label {
  display: block;
  font-family: 'Inter', sans-serif;
  font-weight: 500;
  line-height: 1.25rem;
  margin-bottom: 0.25em;
}

.field input,
.field select {
  background-color: #fff;
  border-color: #e5e7eb;
  border-width: 1px;
  width: 100%;
  padding: 0.5em;
  border-radius: 0.375rem;
  font-family: 'Inter', sans-serif;
  font-weight: 500;
  font-size: 0.875rem;
}

.field input:disabled {
  background-color: #f3f4f6;
  color: #9ca3af;
}

.btn {
  width: 100%;
  padding: 0.5em;
  border: none;
  border-radius: 0.375rem;
  font-family: 'Inter', sans-serif;
  font-weight: 500;
  font-size: 0.875rem;
  cursor: pointer;
  margin-top: 0.5em;
  transition: background-color 0.2s;
}

.button-primary {
  color: #19a311cf;
  background-color: #fff;
}

.button-primary:hover {
  color: #158a0fb5;
}

.button-danger {
  background-color: #fff;
  color: #dc3545;
}

.button-danger:hover {
  color: #c82333;
}
</style>