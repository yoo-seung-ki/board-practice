<template>
  <div id="app">
    <NavigationBar v-if="showNav" />
    <router-view @toggle-nav="toggleNav" />
  </div>
</template>

<script setup>

import { ref } from 'vue';
import NavigationBar from './components/NavigationBar.vue';

const showNav = ref(true);

function toggleNav(show) {
  showNav.value = show;
}

</script>

<style>
/* 글로벌 스타일을 여기에 추가하세요 */
</style>

<script>


</script>

<style>
/* 스타일을 여기에 추가하세요 */
</style>
