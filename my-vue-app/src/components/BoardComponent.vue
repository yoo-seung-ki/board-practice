<template>
  <v-container class="ma-5">
    <h1>Board</h1>
    <div v-if="loading">Loading...</div>
    <div v-else>
      <v-list>
        <v-list-item v-for="post in paginatedPosts" :key="post.id">
          <v-list-item-content>
            <v-list-item-title>{{ post.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <v-pagination
        v-model="currentPage"
        :length="totalPages"
        @input="handlePageChange"
      ></v-pagination>
    </div>
  </v-container>
</template>

<script setup>
import axios from 'axios';
import { ref, computed, onMounted } from 'vue';

const posts = ref([]);
const loading = ref(true);
const currentPage = ref(1);
const postsPerPage = 10;

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

const totalPages = computed(() => {
  return Math.ceil(posts.value.length / postsPerPage);
});

const paginatedPosts = computed(() => {
  const start = (currentPage.value - 1) * postsPerPage;
  const end = start + postsPerPage;
  return posts.value.slice(start, end);
});

const handlePageChange = (page) => {
  currentPage.value = page;
};

onMounted(() => {
  fetchPosts();
});
</script>

<style scoped>
/* 스타일을 여기에 추가하세요 */
</style>
