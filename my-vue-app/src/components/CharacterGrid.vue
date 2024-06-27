<template>
  <v-container>
    <v-row v-for="(value, key, index) in character" :key="index" class="mb-2">
      <v-col cols="6">
        <strong>{{ key }}</strong>
      </v-col>
      <v-col cols="6">
        {{ value }}
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'CharacterGrid',
  props: {
    character: {
      type: Object,
      required: true
    }
  }
};
</script>

<style scoped>
.mb-2 {
  margin-bottom: 1rem;
}
</style>
