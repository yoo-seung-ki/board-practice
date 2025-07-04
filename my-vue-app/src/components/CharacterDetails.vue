<template>
  <v-container>
    <v-row>
      <v-col>
        <h1>{{ characterInfo.characterName }}</h1>
        <div><strong>Job:</strong> {{ characterInfo.jobGrowName }}</div>
        <div><strong>Fame:</strong> {{ characterInfo.fame }}</div>
        <div><strong>Level:</strong> {{ characterInfo.level }}</div>
        <div><strong>Server:</strong> {{ serverName }}</div>
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
        <v-btn class="api-btn my-3 d-block" @click="calculateAllSkillDamage">캐릭터 데미지스킬 계산</v-btn>
      </v-col>
    </v-row>
    <v-progress-circular v-if="isLoading" indeterminate color="primary"></v-progress-circular>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import CharacterGrid from './CharacterGrid.vue';
import getCharacterClass from '@/strategies/Utils/getCharacterClass.js';


const route = useRoute();
const characterInfo = ref({});
const characterImages = ref([]);
const characterStatus = ref({});
const error = ref(null);
const skillList = ref([]);
const skillStyle = ref([]);
const activeSkill = ref([]);
const damageActiveSkill = ref([]);
const passiveSkill = ref([]);

const serverName = ref('');
const isLoading = ref(false);


const fetchCharacterDetails = async () => {

    const serverId = route.params.serverId;
    const characterId = route.params.characterId;
    const serverNameParam = route.query.serverName;
    serverName.value = serverNameParam; // serverName 설정

    // 서버 ID와 캐릭터 ID가 올바르게 전달되는지 확인하기 위해 콘솔에 출력
    console.log(`Fetching details for characterId: ${characterId} on serverId: ${serverId} and serverName: ${serverName.value}`);

    try {
        const response = await axios.get(`/api/df/servers/${serverId}/characters/${characterId}?apikey=${process.env.VUE_APP_API_KEY}`);
        characterInfo.value = response.data;
        console.log('CharacterInfo : ' , characterInfo.value);
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
        //response.data;
        activeSkill.value = response.data.skill.style.active;
        console.log('activeSkill : ', activeSkill);
        passiveSkill.value = response.data.skill.style.passive;
        console.log('passiveSkill : ', passiveSkill);

        for(let i=0; i<activeSkill.value.length; i++) {
            if(activeSkill.value[i].level != 1) {
                let arr = {};
                arr.skillId = activeSkill.value[i].skillId;
                arr.name = activeSkill.value[i].name;
                arr.level = activeSkill.value[i].level;
                damageActiveSkill.value.push(arr);
            }

        }

        const removeSkillIds = [
          '1fea5a626f15230237946a11a9d11582',
          'a6c8f69107f8c4f5d1a0c7a57d000290',
          '04883563896fe1adac7505c6146b5f59',
          '1b1cfab062e0768bcc889e33e1f30dbf',
          '0ed3148658fe37b3336ccb718dc0fdb0'
        ];
        damageActiveSkill.value = damageActiveSkill.value.
                filter(skill => !removeSkillIds.includes(skill.skillId));

        console.log('damageActiveSkill : ', damageActiveSkill);

        error.value = null;
    } catch(err) {
        console.error('Error fetching character skill style : ', err);
        error.value = '캐릭터 스킬 상세정보를 가져오는 중 오류가 발생했습니다.';
    }
}

const calculateAllSkillDamage = async () => {
    isLoading.value = true;
    try {
        const damageResults = [];

        // damageActiveSkill 배열에 있는 모든 스킬에 대해 반복
        for (let i = 0; i < damageActiveSkill.value.length; i++) {
            const skill = damageActiveSkill.value[i];

            // 각 스킬의 데미지를 계산하기 위해 calculateSkillDamage 호출
            const damage = await calculateSkillDamage(skill.skillId, skill.level);

            if (damage !== null) {
                damageResults.push({
                    name: skill.name,
                    level: skill.level,
                    damage: damage
                });
            }
        }

        console.log('Damage Results:', damageResults);
        return damageResults;

    } catch (err) {
        console.error('Error calculating skill damages:', err);
        return null;
    } finally {
        isLoading.value = false;
    }
};


const calculateSkillDamage = async (skillId, skillLevel) => {

    const jobId = characterInfo.value.jobId;
    const jobGrowId = characterInfo.value.jobGrowId;

    const CharacterClass = await getCharacterClass(jobId, jobGrowId);
    if (CharacterClass) {
        const characterInstance = new CharacterClass(characterInfo, fetchSkillDetails);  // characterInfo 전달
        await characterInstance.initialize(); // 필요한 패시브 스킬들 초기화

        const skillDetails = await fetchSkillDetails(jobId, skillId);
        if (!skillDetails) {
            console.error('스킬 정보를 가져오는 데 실패했습니다.');
            return null;
        }

        const levelInfo = skillDetails.levelInfo.rows.find(row => row.level === skillLevel);
        if (!levelInfo) {
            console.error('해당 스킬 레벨 정보를 찾을 수 없습니다.');
            return null;
        }

        return characterInstance.calculateDamage(skillId, levelInfo);
    } else {
        console.error('해당 전직에 맞는 클래스를 찾을 수 없습니다.');
        return null;
    }

};

const fetchSkillDetails = async (jobId, skillId) => {
    try {
        const response = await axios.get(`/api/df/skills/${jobId}/${skillId}?apikey=${process.env.VUE_APP_API_KEY}`);
        return response.data; // API 응답 데이터를 반환
    } catch (error) {
        console.error(`Error fetching skill details for skillId: ${skillId}`, error);
        return null; // 에러 발생 시 null 반환
    }
};



onMounted(() => {
  fetchCharacterDetails();
});
</script>

<style scoped>
.mb-3 {
  margin-bottom: 1rem;
}
</style>
