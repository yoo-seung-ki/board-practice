import axios from 'axios';

export default class NenMasterFemale {

    constructor(characterInfo, fetchSkillDetails) {
        this.characterInfo = characterInfo;
        this.fetchSkillDetails = fetchSkillDetails; // fetchSkillDetails 함수 저장
        this.hwanyoungInfo = null; // 초기화
        this.initializing = null; // 초기화 상태 추적
        //this.skillData = null; // 캐시된 스킬 데이터
    }

    async initialize() {
        if (this.initializing) {
            return this.initializing; // 이미 초기화 중이면 해당 Promise 반환
        }
        this.initializing = this.loadCharacterSkillStyle();
        try {
            await this.initializing;
        } catch (error) {
            console.error('Initialization failed:', error);
            throw error; // 필요시 에러를 다시 던져 상위에서 처리
        } finally {
            this.initializing = null; // 초기화 완료 후 플래그 해제
        }
    }

    async loadCharacterSkillStyle() {
        try {
            const response = await axios.get(`/api/df/servers/${this.characterInfo.value.serverId}/characters/${this.characterInfo.value.characterId}/skill/style?apikey=${process.env.VUE_APP_API_KEY}`);
            this.skillData = response.data.skill; // 데이터를 캐시에 저장
            this.characterInfo.skill = this.skillData; // characterInfo에 skill 데이터 추가
        } catch (error) {
            throw new Error('JS 캐릭터 스킬 스타일을 불러오는 데 실패했습니다.');
        }
    }

    async loadHwanyoungInfoIfNeeded() {
        // 환영폭쇄 정보가 없을 때만 가져오기
        if (!this.hwanyoungInfo) {
            this.hwanyoungInfo = await this.getHwanyoungExplosionInfo();
        }
    }

    async getHwanyoungExplosionInfo() {
        // characterskillstyle API로부터 패시브 스킬 정보를 가져옴
        const passiveSkills = this.characterInfo.skill.style.passive;
        console.log('Passive Skills:', passiveSkills);
        const hwanyoungSkill = passiveSkills.find(skill => skill.name === '환영폭쇄');
        console.log('Hwanyoung Skill:', hwanyoungSkill);

        if (hwanyoungSkill) {
            const skillDetails = await this.fetchSkillDetails(this.characterInfo.value.jobId, hwanyoungSkill.skillId);
            console.log('Hwanyoung Skill Details:', skillDetails);
            const hwanyoungLevelInfo = skillDetails.levelInfo.rows.find(row => row.level === hwanyoungSkill.level);
            console.log('Hwanyoung Level Info:', hwanyoungLevelInfo);

            return hwanyoungLevelInfo;
        }

        console.error('환영폭쇄 스킬을 찾을 수 없습니다.');
        return null;
    }

    async calculateDamage(skillId, levelInfo) {
        switch(skillId) {
            case 'f2fb27162beb0b87a7cb9af7900e95f2' : // 분신
                await this.loadHwanyoungInfoIfNeeded();
                return this.calculateBunshinDamage(levelInfo);
            case '4c5271b0ecce120d7fc113f377fae76f' : // 뇌전격
                return this.calculateNoejeongyeok(levelInfo);
            case '2e2b7efe778656690f9c8cb6e47c3932' : // 비룡나선파
                return this.calculateBiryongNaseonpa(levelInfo);
            case '9cb6f9ed646fa87f9b7680a42ce83d1a' : // 사자후
                return this.calculateSajahu(levelInfo);
            case '4224f9b0b8c7c903e9a1e0f9d9f6d04d' : // 기공장
                return this.calculateGigongjang(levelInfo);
            case 'dbf8b30c7057032af0d68fcfa289fdae' : // 광룡승천파
                return this.calculateGwangnyongSeungcheonpa(levelInfo);
            case '51a08fd0c90f0a5276cd552047fac93d' : // 넨화
                return this.calculateFirstAwakeningSkill(levelInfo);
            case '5806440d21e7546d50007a5ba11f8024' : // 광룡십삼장
                return this.calculateGwangryongSipsamjang(levelInfo);
            case '7cf17936a039b418660424125dc968d7' : // 태사자후
                return this.calculateTaesajahu(levelInfo);
            case 'a5fa08f5d509e6ff2ebc68856a470b5a' : // 섬광환격
                return this.calculateSeomgwangHwangeok(levelInfo);
            case '506e7ed77d517419a6e1c437a2cedb17' : // 흡기공탄
                return this.calculateHeupgigongtan(levelInfo);
            case '5dc7008b12a459325b548b0715c6b73c' : // 광휘의 넨
                return this.calculateSecondAwakeningSkill(levelInfo);
            case '85f7c810ad503790e8626439fe936d56' : // 천뢰분신보
                return this.calculateCheonroeBunshinbo(levelInfo);
            case '002cbdd9bfd0f0b970451ae8d48d029e' : // 염인환류파-천지개벽
                return this.calculateNeoAwakeningSkill(levelInfo);
            //   Yeominhwanryupa Cheonjigaebyeok
            // 다른 스킬들도 추가 가능
            default:
                console.error('Unknown skill ID:', skillId);
                return null;
        }
    }


    calculateBunshinDamage(levelInfo) {
        // 환영폭쇄 정보를 가지고 있지 않다면 null을 반환
        if (!this.hwanyoungInfo || !this.hwanyoungInfo.optionValue) {
            console.error('환영폭쇄 정보를 찾을 수 없습니다. 분신 스킬은 데미지가 없습니다.');
            return null;
        }

        // 환영폭쇄 스킬의 정보를 사용하여 추가 데미지를 계산
        const hwanyoungDamageMultiplier = this.hwanyoungInfo.optionValue.value1; // 분신 1체당 해당 레벨의 환영폭쇄 데미지 배율
        const bunshinDamage = levelInfo.optionValue.value2; // 분신 생성 요소

        return bunshinDamage * hwanyoungDamageMultiplier;
    }

    calculateNoejeongyeok(levelInfo) { // 뇌전격
         const baseDamage = levelInfo.optionValue.value1;
         return baseDamage;
    }

    calculateBiryongNaseonpa(levelInfo) { // 비룡나선파
        const baseDamage = levelInfo.optionValue.value1 * levelInfo.optionValue.value2 + levelInfo.optionValue.value3 * levelInfo.optionValue.value4;
        return baseDamage;
    }

    calculateSajahu(levelInfo) { // 사자후
        const baseDamage = levelInfo.optionValue.value1;
        return baseDamage;
    }

    calculateGigongjang(levelInfo) { // 기공장
        const baseDamage = levelInfo.optionValue.value1 * levelInfo.optionValue.value5;
        return baseDamage;
    }

    calculateGwangnyongSeungcheonpa(levelInfo) { // 광룡승천파
        const baseDamage = levelInfo.optionValue.value1 * levelInfo.optionValue.value2;
        return baseDamage;
    }

    calculateFirstAwakeningSkill(levelInfo) { // 흩날리는 천염화
        const baseDamage = levelInfo.optionValue.value8;
        return baseDamage;
    }

    calculateGwangryongSipsamjang(levelInfo) { // 광룡십삼장
        const baseDamage = levelInfo.optionValue.value1 * 6 + levelInfo.optionValue.value2;
        return baseDamage;
    }

    calculateTaesajahu(levelInfo) { // 태사자후
        const baseDamage = levelInfo.optionValue.value1 * 5;
        return baseDamage;
    }

    calculateSeomgwangHwangeok(levelInfo) { // 섬광환격
        const baseDamage = levelInfo.optionValue.value1 * 4;
        return baseDamage;
    }

    calculateHeupgigongtan(levelInfo) { // 흡기공탄
        const baseDamage = levelInfo.optionValue.value3 * levelInfo.optionValue.value4;
        return baseDamage;
    }

    calculateSecondAwakeningSkill(levelInfo) { // 낙화태염성
        const baseDamage = levelInfo.optionValue.value2;
        return baseDamage;
    }

    calculateCheonroeBunshinbo(levelInfo) { // 천뢰분신보
        const baseDamage = levelInfo.optionValue.value1 + levelInfo.optionValue.value2 * 10 + levelInfo.optionValue.value3;
        return baseDamage;
    }

    calculateNeoAwakeningSkill(levelInfo) { // 염인환류파 - 천지개벽
        const baseDamage = levelInfo.optionValue.value1 * 5 + levelInfo.optionValue.value2 * 5 + levelInfo.optionValue.value3
        + levelInfo.optionValue.value4 + levelInfo.optionValue.value5;
        return baseDamage;
    }

}