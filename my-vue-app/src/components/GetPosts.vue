<template>
  <v-container class="ma-5">
    <h1>All Posts</h1>
    <div v-if="loading">Loading...</div>
    <div v-else>
      <ul>
        <li v-for="post in posts" :key="post.id">
          <h2>{{ post.title }}</h2>
          <p>{{ post.content }}</p>
        </li>
      </ul>
    </div>
  </v-container>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';

const posts = ref([]);
const loading = ref(true);

const fetchPosts = async () => {
  try {
    const response = await axios.get('http://localhost:8080/Posts/posts');
    posts.value = response.data;
    loading.value = false;
  } catch (error) {
    console.error('Error fetching data:', error);
    loading.value = false;
  }
};

onMounted(() => {
  fetchPosts();
});
</script>

<style scoped>
/* 스타일을 여기에 추가하세요 */
</style>
