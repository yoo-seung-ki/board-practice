<template>
  <div>
    <v-btn @click="$router.push('/ExampleComponent')">Go to Example Component</v-btn>
    <v-btn @click="$router.push('/ConstrainedFrame')">Go to ConstrainedFrame</v-btn>
  </div>
</template>

<script>
export default {
  name: 'NavigationBar',
};
</script>

<style scoped>
/* 네비게이션 바 스타일을 여기에 추가하세요 */
</style>
