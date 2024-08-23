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
        <v-btn calss="api-btn my-3 d-block" @click="calculateDamageSkill">캐릭터 데미지스킬 계산</v-btn>
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
const activeSkill = ref([]);
const damageActiveSkill = ref([]);
const passiveSkill = ref([]);
//const damageSkill = ref([]);

/*
const jobMapping = {
    "41f1cdc2ff58bb5fdc287be0db2a8df3" : {                          // 귀검사(남)
        "37495b941da3b1661bc900e68ef3b2c6" : WeaponMaster,          // 웨펀마스터
        "618326026de1a1f1cfba5dbd0b8396e7" : SoulBringer,           // 소울브링어
        "6d459bc74ba73ee4fe5cdc4655400193" : Berserker,             // 버서커
        "c9b492038ee3ca8d27d7004cf58d59f3" : Asura,                 // 아수라
        "92da05ec93fb43406e193ffb9a2a629b" : Ghostblade             // 검귀
    },
    "a7a059ebe9e6054c0644b40ef316d6e9" : {                          // 격투가(여)
        "37495b941da3b1661bc900e68ef3b2c6" : NenMasterFemale,       // 넨마스터(여)
        "618326026de1a1f1cfba5dbd0b8396e7" : StrikerFemale,         // 스트라이커(여)
        "6d459bc74ba73ee4fe5cdc4655400193" : StreetFighterFemale,   // 스트리트파이터(여)
        "c9b492038ee3ca8d27d7004cf58d59f3" : GrapplerFemale         // 그래플러(여)
    },
    "afdf3b989339de478e85b614d274d1ef" : {                          // 거너(남)
        "37495b941da3b1661bc900e68ef3b2c6" : RangerMale,            // 레인저(남)
        "618326026de1a1f1cfba5dbd0b8396e7" : LauncherMale,          // 런처(남)
        "6d459bc74ba73ee4fe5cdc4655400193" : MechanicMale,          // 메카닉(남)
        "c9b492038ee3ca8d27d7004cf58d59f3" : SpitfireMale,          // 스핏파이어(남)
        "92da05ec93fb43406e193ffb9a2a629b" : Assault                // 어썰트
    },
    "3909d0b188e9c95311399f776e331da5" : {                          // 마법사(여)
        "37495b941da3b1661bc900e68ef3b2c6" : ElementalMaster,       // 엘레멘탈마스터
        "618326026de1a1f1cfba5dbd0b8396e7" : Summoner,              // 소환사
        "6d459bc74ba73ee4fe5cdc4655400193" : BattleMage,            // 배틀메이지
        "c9b492038ee3ca8d27d7004cf58d59f3" : Witch,                 // 마도학자
        "92da05ec93fb43406e193ffb9a2a629b" : Enchantress            // 인챈트리스
    },
    "f6a4ad30555b99b499c07835f87ce522" : {                          // 프리스트(남)
        "37495b941da3b1661bc900e68ef3b2c6" : CrusaderMale,          // 크루세이더(남)
        "618326026de1a1f1cfba5dbd0b8396e7" : Infighter,             // 인파이터
        "6d459bc74ba73ee4fe5cdc4655400193" : Exorcist,              // 퇴마사
        "c9b492038ee3ca8d27d7004cf58d59f3" : Avenger                // 어벤저
    },
    "944b9aab492c15a8474f96947ceeb9e4" : {                          // 거너(여)
        "37495b941da3b1661bc900e68ef3b2c6" : RangerFemale,          // 레인저(여)
        "618326026de1a1f1cfba5dbd0b8396e7" : LauncherFemale,        // 런처(여)
        "6d459bc74ba73ee4fe5cdc4655400193" : MechanicFemale,        // 메카닉(여)
        "c9b492038ee3ca8d27d7004cf58d59f3" : SpitfireFemale         // 스핏파이어(여)
    },
    "ddc49e9ad1ff72a00b53c6cff5b1e920" : {                          // 도적(여)
        "37495b941da3b1661bc900e68ef3b2c6" : Rogue,                 // 로그
        "618326026de1a1f1cfba5dbd0b8396e7" : Necromancer,           // 사령술사
        "6d459bc74ba73ee4fe5cdc4655400193" : Kunoichi,              // 쿠노이치
        "c9b492038ee3ca8d27d7004cf58d59f3" : ShadowDancer           // 섀도우댄서
    },
    "ca0f0e0e9e1d55b5f9955b03d9dd213c" : {                          // 격투가(남)
        "37495b941da3b1661bc900e68ef3b2c6" : NenMasterMale,         // 넨마스터(남)
        "618326026de1a1f1cfba5dbd0b8396e7" : StrikerMale,           // 스트라이커(남)
        "6d459bc74ba73ee4fe5cdc4655400193" : StreetFighterMale,     // 스트리트파이터(남)
        "c9b492038ee3ca8d27d7004cf58d59f3" : GrapplerMale           // 그래플러(남)
    },
    "a5ccbaf5538981c6ef99b236c0a60b73" : {                          // 마법사(남)
        "37495b941da3b1661bc900e68ef3b2c6" : ElementalBomber,       // 엘리멘탈 바머
        "618326026de1a1f1cfba5dbd0b8396e7" : GlacialMaster,         // 빙결사
        "6d459bc74ba73ee4fe5cdc4655400193" : BloodMage,             // 블러드 메이지
        "c9b492038ee3ca8d27d7004cf58d59f3" : SwiftMaster,           // 스위프트 마스터
        "92da05ec93fb43406e193ffb9a2a629b" : DimensionWalker        // 디멘션 워커
    },
    "17e417b31686389eebff6d754c3401ea" : {                          // 다크나이트
        "0a49d9c8b5e1358efff324e5cb11d41e" : DarkKnight             // 다크나이트
    },
    "b522a95d819a5559b775deb9a490e49a" : {                          // 크리에이터
        "0a49d9c8b5e1358efff324e5cb11d41e" : Creator                // 크리에이터
    },
    "1645c45aabb008c98406b3a16447040d" : {                          // 귀검사(여)
        "37495b941da3b1661bc900e68ef3b2c6" : SwordMaster,           // 소드마스터
        "618326026de1a1f1cfba5dbd0b8396e7" : DarkTemplar,           // 다크템플러
        "6d459bc74ba73ee4fe5cdc4655400193" : DemonSlayer,           // 데몬슬레이어
        "c9b492038ee3ca8d27d7004cf58d59f3" : Vagabond,              // 베가본드
        "92da05ec93fb43406e193ffb9a2a629b" : Blade                  // 블레이드
    },
    "0ee8fa5dc525c1a1f23fc6911e921e4a" : {                          // 나이트
        "37495b941da3b1661bc900e68ef3b2c6" : ElvenKnight,           // 엘븐나이트
        "618326026de1a1f1cfba5dbd0b8396e7" : Chaos,                 // 카오스
        "6d459bc74ba73ee4fe5cdc4655400193" : Paladin,               // 팔라딘
        "c9b492038ee3ca8d27d7004cf58d59f3" : DragonKnight           // 드래곤나이트
    },
    "3deb7be5f01953ac8b1ecaa1e25e0420" : {                          // 마창사
        "37495b941da3b1661bc900e68ef3b2c6" : Vanguard,              // 뱅가드
        "618326026de1a1f1cfba5dbd0b8396e7" : Duelist,               // 듀얼리스트
        "6d459bc74ba73ee4fe5cdc4655400193" : DragonianLancer,       // 드래고니안 랜서
        "c9b492038ee3ca8d27d7004cf58d59f3" : DarkLancer             // 다크 랜서
    },
    "0c1b401bb09241570d364420b3ba3fd7" : {                          // 프리스트(여)
        "37495b941da3b1661bc900e68ef3b2c6" : CrusaderFemale,        // 크루세이더(여)
        "618326026de1a1f1cfba5dbd0b8396e7" : Inquisitor,            // 이단심판관
        "6d459bc74ba73ee4fe5cdc4655400193" : Sorceress,             // 무녀
        "c9b492038ee3ca8d27d7004cf58d59f3" : Mistress               // 미스트리스
    },
    "986c2b3d72ee0e4a0b7fcfbe786d4e02" : {                          // 총검사
        "37495b941da3b1661bc900e68ef3b2c6" : Hitman,                // 히트맨
        "618326026de1a1f1cfba5dbd0b8396e7" : Agent,                 // 요원
        "6d459bc74ba73ee4fe5cdc4655400193" : TroubleShooter,        // 트러블 슈터
        "c9b492038ee3ca8d27d7004cf58d59f3" : Specialist             // 스페셜리스트
    },
    "b9cb48777665de22c006fabaf9a560b3" : {                          // 아처
        "37495b941da3b1661bc900e68ef3b2c6" : Muse,                  // 뮤즈
        "618326026de1a1f1cfba5dbd0b8396e7" : Traveler,              // 트래블러
        "6d459bc74ba73ee4fe5cdc4655400193" : Hunter,                // 헌터
        "c9b492038ee3ca8d27d7004cf58d59f3" : Vigilante              // 비질란테
    }

} */

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
          '1b1cfab062e0768bcc889e33e1f30dbf'
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

const calculateDamageSkill = async () => {
    try {

        const response = await axios.get(`/api/df/skills/${characterInfo.value.jobId}/${damageActiveSkill.value[0].skillId}?apikey=${process.env.VUE_APP_API_KEY}`);
        console.log('check : ' , response.data);
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
