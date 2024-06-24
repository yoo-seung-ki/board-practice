<template>
  <v-container class="ma-5">
    <v-row>
      <v-col>
      <v-btn @click="goToHome">Back to Home</v-btn>
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

import { ref, onMounted, onUnmounted, getCurrentInstance } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

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

const { emit } = getCurrentInstance();
const router = useRouter();

const goToHome = () => {
  router.push('/');
};

onMounted(() => {
  fetchUsers();
  emit('toggle-nav', false); // 네비게이션 바를 숨김
});

onUnmounted(() => {
  emit('toggle-nav', true); // 네비게이션 바를 다시 표시
});

</script>

<style scoped>
.user-list {
  margin-left: 20px; /* 추가적인 왼쪽 여백 */
}
</style>
