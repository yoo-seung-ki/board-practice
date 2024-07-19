<template>
  <v-container class="ma-5">
    <!-- API 키 입력 텍스트박스 -->
    <v-text-field
      v-model="apiKey"
      label="Enter API Key"
      outlined
      class="mb-1"
    ></v-text-field>

    <!-- API Key 변경 버튼 -->
    <v-btn class="mb-5" @click="updateApiKey">API Key 변경</v-btn>

    <!-- 서버 선택 드롭다운 메뉴 -->
    <v-select
      v-model="selectedServer"
      :items="serversWithAll"
      item-title="serverName"
      item-value="serverId"
      label="서버 선택(전체를 선택하면 모든 서버 검색)"
      outlined
      class="my-3"
    ></v-select>

    <!-- 캐릭터 닉네임 입력 텍스트박스 -->
    <v-text-field
      v-model="characterNickname"
      label="Enter Character Nickname"
      outlined
      class="mb-1"
    ></v-text-field>

    <!-- 요청 버튼 -->
    <v-btn class="api-btn my-3" @click="fetchCharacterInfo">캐릭터 기본정보 및 이미지 불러오기</v-btn>

    <!-- CharacterSearchResults 컴포넌트 사용 -->
    <CharacterSearchResults v-if="searchResults.length" :searchResults="searchResults" />

    <!-- 에러 메시지 표시 -->
    <v-alert v-if="error" type="error" class="mt-3">
      {{ error }}
    </v-alert>
  </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import CharacterSearchResults from './CharacterSearchResults.vue';

const apiKey = ref(process.env.VUE_APP_API_KEY || '');
const servers = ref([]);
const selectedServer = ref('');
const characterNickname = ref('');
const error = ref(null);
const searchResults = ref([]);

const serversWithAll = computed(() => [
  { serverId: 'all', serverName: '전체' },
  ...servers.value
]);

const fetchServers = async () => {
  if (!apiKey.value) return;

  try {
    const response = await axios.get(`/api/df/servers?apikey=${apiKey.value}`);
    servers.value = response.data.rows;
    error.value = null;
  } catch (err) {
    console.error('Error fetching server data:', err);
    error.value = '서버 데이터를 가져오는 중 오류가 발생했습니다. API 키를 확인하세요.';
  }
};

const fetchCharacterInfo = async () => {
  if (!apiKey.value || !characterNickname.value) {
    error.value = 'API Key와 Character Nickname은 필수 항목입니다.';
    return;
  }

  try {
    error.value = null;
    searchResults.value = [];

    if (selectedServer.value && selectedServer.value !== 'all') {
      const response = await axios.get(`api/df/servers/${selectedServer.value}/characters?characterName=${encodeURIComponent(characterNickname.value)}&apikey=${apiKey.value}`);
      const characters = response.data.rows.map(character => ({
        characterId: character.characterId, // characterId 추가
        characterName: character.characterName,
        jobGrowName: character.jobGrowName,
        fame: character.fame,
        level: character.level,
        serverName: getServerName(selectedServer.value),
        serverId: selectedServer.value
      }));
      searchResults.value.push(...characters);
    } else {
      for (const server of servers.value) {
        const response = await axios.get(`api/df/servers/${server.serverId}/characters?characterName=${encodeURIComponent(characterNickname.value)}&apikey=${apiKey.value}`);
        const characters = response.data.rows.map(character => ({
          characterId: character.characterId, // characterId 추가
          characterName: character.characterName,
          jobGrowName: character.jobGrowName,
          fame: character.fame,
          level: character.level,
          serverName: server.serverName,
          serverId: server.serverId
        }));
        searchResults.value.push(...characters);
      }
    }
  } catch (err) {
    console.error('Error fetching character info:', err);
    error.value = '캐릭터 정보를 가져오는 중 오류가 발생했습니다. 입력 값을 확인하세요.';
  }
};

const getServerName = (serverId) => {
  const server = servers.value.find(server => server.serverId === serverId);
  return server ? server.serverName : serverId;
};

const updateApiKey = () => {
  fetchServers();
};

onMounted(() => {
  if (apiKey.value) {
    fetchServers();
  }
});
</script>

<style scoped>
.my-3 {
  margin-top: 1rem;
  margin-bottom: 1rem;
}

.mb-1 {
  margin-bottom: 0.5rem;
}

.mb-5 {
  margin-bottom: 3rem !important;
}

.mt-3 {
  margin-top: 1rem;
}

.mt-5 {
  margin-top: 3rem;
  margin-bottom: 3rem;
}
</style>
