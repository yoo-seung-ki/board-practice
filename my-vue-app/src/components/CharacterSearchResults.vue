<template>
  <v-container>
    <v-row>
      <v-col
        v-for="character in searchResults"
        :key="character.characterId"
        cols="12" sm="6" md="4"
      >
        <v-card>
          <v-card-title>{{ character.characterName }}</v-card-title>
          <v-card-subtitle>{{ character.jobGrowName }}</v-card-subtitle>
          <v-card-text>
            <div><strong>명성:</strong> {{ character.fame }}</div>
            <div><strong>레벨:</strong> {{ character.level }}</div>
            <div><strong>서버:</strong> {{ character.serverName }}</div>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="viewDetails(character.serverId, character.characterId)">View Details</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { useRouter } from 'vue-router';

// Props 정의
const props = defineProps({
  searchResults: {
    type: Array,
    required: true
  }
});

const router = useRouter();

const viewDetails = (serverId, characterId) => {
  console.log(`Navigating to details of characterId: ${characterId} on serverId: ${serverId}`);
  router.push({ name: 'CharacterDetails', params: { serverId, characterId } });
};
</script>

<style scoped>
.v-card {
  margin-bottom: 20px;
}
</style>
