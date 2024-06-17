<template>
  <v-container class="ma-5">
    <v-row>
      <v-col>
        <h1>Users</h1>
        <ul>
          <li v-for="user in users" :key="user.id">{{ user.name }}</li>
        </ul>
        <div v-if="loading">Loading...</div>
        <div v-else-if="!users.length">No users found.</div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const users = ref([]);
const loading = ref(true);

const fetchUsers = async () => {
  try {
    const response = await axios.get('http://localhost:8080/Users/users');
    users.value = response.data;
    loading.value = false;
  } catch (error) {
    console.error('Error fetching data:', error);
    loading.value = false;
  }
};

onMounted(fetchUsers);
</script>

<style scoped>
.user-list {
  margin-left: 20px; /* 추가적인 왼쪽 여백 */
}
</style>
