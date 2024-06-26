<template>
  <v-container class="ma-5">
    <h1>URL Encoder</h1>
    <v-text-field
      v-model="inputText"
      label="Enter text"
      outlined
    ></v-text-field>
    <v-btn @click="encodeText">Encode</v-btn>
    <div v-if="encodedText">
      <h2>Encoded Text</h2>
      <p>{{ encodedText }}</p>
    </div>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';

const inputText = ref('');
const encodedText = ref('');

const encodeText = () => {
  encodedText.value = encodeURIComponent(inputText.value);
};
</script>

<style scoped>
/* 스타일을 여기에 추가하세요 */
</style>
