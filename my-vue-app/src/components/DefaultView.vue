
<template>
  <v-container class="ma-5">
    <!-- 텍스트박스 추가 -->
    <v-text-field
      v-model="apiKey"
      label="Enter API Key"
      outlined
      class="mb-1"
    ></v-text-field>

    <!-- API Key 변경 버튼 추가 -->
    <v-btn class="mb-5" @click="updateApiKey">API Key 변경</v-btn>

    <!-- 서버 선택 드롭다운 메뉴 추가 -->
    <v-select
      v-model="selectedServer"
      :items="servers"
      item-title="serverName"
      item-value="serverId"
      label="서버 선택"
      outlined
      class="my-3"
      :disabled="!servers.length"
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

    <!-- 캐릭터 이미지 표시 -->
    <div v-if="characterImages.length" class="mt-5">
      <h3>Character Images</h3>
      <div v-for="(image, index) in characterImages" :key="index" class="mb-3">
        <img :src="image" :alt="'Character Image ' + (index + 1)" />
      </div>
    </div>

    <!-- 캐릭터 능력치 정보 불러오기 버튼-->
    <div class="my-3">
        <v-btn @click="fetchCharacterStatus">캐릭터 능력치 정보 불러오기</v-btn>
    </div>

    <!-- CharacterGrid 컴포넌트 사용 -->
        <CharacterGrid v-if="characterStatus" :character="characterStatus" />

    <!-- 에러 메시지 표시 -->
    <v-alert v-if="error" type="error" class="mt-3">
      {{ error }}
    </v-alert>

  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import CharacterGrid from './CharacterGrid.vue';

const apiKey = ref(process.env.VUE_APP_API_KEY || '');
const servers = ref([]);
const selectedServer = ref('');
const characterNickname = ref(''); // 캐릭터 닉네임
const error = ref(null);
const characterImages = ref([]);
const characterId = ref(''); // 캐릭터 ID를 저장할 변수
const characterStatus = ref({});

const selectServers = async () => {
  if (!apiKey.value) return; // API 키가 없으면 요청을 보내지 않음

  try {
    const response = await axios.get(`/api/df/servers?apikey=${apiKey.value}`);
    servers.value = response.data.rows;
    error.value = null; // Clear error if fetch is successful
  } catch (err) {
    console.error('Error fetching server data:', err);
    error.value = 'Error fetching server data. Please check your API key.';
  }
};

const fetchCharacterInfo = async () => {
  if (!apiKey.value || !selectedServer.value || !characterNickname.value) {
    error.value = 'API Key, Server, and Character Nickname are required';
    return;
  }

  try {
    const response = await axios.get(`api/df/servers/${selectedServer.value}/characters?characterName=${characterNickname.value}&apikey=${apiKey.value}`);
    console.log('Character Info : ', response.data); // 응답 데이터 확인용 콘솔 출력
    characterId.value  = response.data.rows[0].characterId;
    fetchCharacterImages(characterId);
    error.value = null; // 성공 시 에러 메시지 초기화
  } catch (err) {
    console.error('Error fetching character info:', err);
    error.value = 'Error fetching character info. Please check your input values and API key.';
  }
};


const fetchCharacterImages = () => {
    if(!characterId.value) {
        error.value = 'Character ID is required to fetch images';
        return;
    }
  const serverId = selectedServer.value;
  characterImages.value = [1, 2, 3].map(zoom =>
    `https://img-api.neople.co.kr/df/servers/${serverId}/characters/${characterId.value}?zoom=${zoom}`
  );
};


const fetchCharacterStatus = async () => {
    try {
        const response = await axios.get(`api/df/servers/${selectedServer.value}/characters/${characterId.value}/status?apikey=${apiKey.value}`);
        console.log('Character Status : ', response.data);
        characterStatus.value = response.data;
        error.value = null;
    } catch(err) {
        console.error('Error fetching character status:', err);
        error.value = 'Error fetching character status. Please check your input values and API key.';
    }
}



const updateApiKey = () => {
  selectServers();
};

onMounted(() => {
  if (apiKey.value) {
    selectServers();
  }
});

</script>

<style scoped>
/* 스타일을 여기에 추가하세요 */

.my-3 {
  margin-top: 1rem;
  margin-bottom: 1rem;
}

.mb-1 {
  margin-bottom: 0.5rem; /* 텍스트박스와 버튼 사이 간격을 좁힘 */
}

.mb-5 {
  margin-bottom: 3rem !important; /* 버튼과 셀렉트 박스 사이 간격을 넓힘 */
}

.mt-3 {
  margin-top: 1rem;
}

.mt-5 {
  margin-top: 3rem;
  margin-bottom: 3rem;
}
</style>
