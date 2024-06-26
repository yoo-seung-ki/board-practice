
<template>
  <div>
    <v-btn @click="$router.push('/ExampleComponent')">Go to Example Component</v-btn>
    <v-btn @click="$router.push('/ConstrainedFrame')">Go to ConstrainedFrame</v-btn>
    <v-btn @click="$router.push('/defaultview')">Go to Default View</v-btn>
    <v-btn @click="goToHome">Back to Home</v-btn>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goToHome = () => {
  router.push('/');
};
</script>



<style scoped>
/* 네비게이션 바 스타일을 여기에 추가하세요 */
</style>
