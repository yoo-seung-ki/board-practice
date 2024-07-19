<template>
  <v-container>
    <v-row>
      <v-col>
        <h1>{{ characterInfo.characterName }}</h1>
        <div><strong>Job:</strong> {{ characterInfo.jobGrowName }}</div>
        <div><strong>Fame:</strong> {{ characterInfo.fame }}</div>
        <div><strong>Level:</strong> {{ characterInfo.level }}</div>
        <div><strong>Server:</strong> {{ characterInfo.serverName }}</div>
        <div v-if="characterImages.length">
          <h3>Character Images</h3>
          <div v-for="(image, index) in characterImages" :key="index" class="mb-3">
            <img :src="image" :alt="'Character Image ' + (index + 1)" />
          </div>
        </div>
        <v-btn @click="fetchCharacterStatus">Fetch Character Status</v-btn>
        <CharacterGrid v-if="characterStatus" :character="characterStatus" />
        <!-- 캐릭터 스킬 리스트 불러오기 버튼 -->
        <v-btn class="api-btn my-3 d-block" @click="fetchSkillList">캐릭터 스킬 리스트 불러오기</v-btn>
        <v-btn class="api-btn my-3 d-block" @click="characterSkillStyle">캐릭터 스킬스타일 불러오기</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import CharacterGrid from './CharacterGrid.vue';

const route = useRoute();
const characterInfo = ref({});
const characterImages = ref([]);
const characterStatus = ref({});
const error = ref(null);
const skillList = ref([]);
const skillStyle = ref([]);
const damageSkill = ref([]);

const fetchCharacterDetails = async () => {
  const serverId = route.params.serverId;
  const characterId = route.params.characterId;
  // 서버 ID와 캐릭터 ID가 올바르게 전달되는지 확인하기 위해 콘솔에 출력
  console.log(`Fetching details for characterId: ${characterId} on serverId: ${serverId}`);

  try {
    const response = await axios.get(`/api/df/servers/${serverId}/characters/${characterId}?apikey=${process.env.VUE_APP_API_KEY}`);
    characterInfo.value = response.data;
    characterImages.value = [1, 2, 3].map(zoom =>
      `https://img-api.neople.co.kr/df/servers/${serverId}/characters/${characterId}?zoom=${zoom}`
    );
  } catch (err) {
    console.error('Error fetching character details:', err);
    error.value = 'Error fetching character details.';
  }
};

const fetchCharacterStatus = async () => {
  const serverId = route.params.serverId;
  const characterId = route.params.characterId;
  try {
    const response = await axios.get(`/api/df/servers/${serverId}/characters/${characterId}/status?apikey=${process.env.VUE_APP_API_KEY}`);
    characterStatus.value = response.data;
  } catch (err) {
    console.error('Error fetching character status:', err);
    error.value = 'Error fetching character status.';
  }
};



const fetchSkillList = async () => {

    try {
        const response = await axios.get(`/api/df/skills/${characterInfo.value.jobId}?jobGrowId=${characterInfo.value.jobGrowId}&apikey=${process.env.VUE_APP_API_KEY}`);
        console.log('Skill List:', response.data); // 응답 데이터 출력
        skillList.value = response.data;
        console.log('skillList.value : ', skillList.value);
        console.log('skillList.value.skills[0] : ', skillList.value.skills[0]);
        error.value = null;
    } catch(err) {
        console.error('Error fetching skill list:', err);
        error.value = '스킬 리스트를 가져오는 중 오류가 발생했습니다.';
    }
}

const characterSkillStyle = async() => {
    try {
        const response = await axios.get(`/api/df/servers/${characterInfo.value.serverId}/characters/${characterInfo.value.characterId}/skill/style?apikey=${process.env.VUE_APP_API_KEY}`);
        console.log('Character Skill Style : ', response.data);
        skillStyle.value = response.data.skill.style.active;
        console.log('skillStyle : ', skillStyle);
        for(let i=0; i<skillStyle.value.length; i++) {
            if(skillStyle.value[i].level != 1) {
                let arr = {};
                arr.skillId = skillStyle.value[i].skillId;
                arr.name = skillStyle.value[i].name;
                damageSkill.value.push(arr);
            }

        }
        console.log('damageSkill : ', damageSkill);
        /*
        for(let i=0; i<damageSkill.value.length; i++) {

        } */
        error.value = null;
    } catch(err) {
        console.error('Error fetching character skill style : ', err);
        error.value = '캐릭터 스킬 상세정보를 가져오는 중 오류가 발생했습니다.';
    }
}


onMounted(() => {
  fetchCharacterDetails();
});
</script>

<style scoped>
.mb-3 {
  margin-bottom: 1rem;
}
</style>
