import axios from 'axios';

// WeaponMaster.js
export default class WeaponMaster {
    constructor(characterInfo, fetchSkillDetails) {
        this.characterInfo = characterInfo;
        this.fetchSkillDetails = fetchSkillDetails; // fetchSkillDetails 함수 저장
        this.initializing = null; // 초기화 상태 추적
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

    async calculateDamage(skillId, levelInfo) {
        switch(skillId) {
            case '51a08fd0c90f0a5276cd552047fac93d' : // 리귀검술
                return this.calculateRiGwigeomsul(levelInfo);
            case '2a0a39184de92acf1c1375e00b77404c' : // 류심 충
                return this.calculateRyusimChung(levelInfo);
            case '9dc8438e4572d39243c97da31c113acc' : // 류심 쾌
                return this.calculateRyusimKae(levelInfo);
            case '28b583c75a49103a1d8aabf799c000a4' : // 차지 크래시
                return this.calculateChajiKeuraeshi(levelInfo);
            case '3fb8395ae3b81bd608e0c4223a8eb534' : // 류심 승
                return this.calculateRyusimSeung(levelInfo);
            case 'cfacda0647b9a0f595df2c2aad30c18d' : // 발도
                return this.calculateBaldo(levelInfo);
            case '669f1428193f61f9d92c743b72438c4d' : // 맹룡단공참
                return this.calculateMaengryongDangongcham(levelInfo);
            case '2c9d9a36c8401bddff6cdb80fab8dc24' : // 차지 버스트
                return this.calculateChajiBeoseuteu(levelInfo);
            case '8510294202d0e042dd29a2422fc6770d' : // 환영검무
                return this.calculateHwanyeongGeommu(levelInfo);
            case '4f2e001e9a19eb7bae50ad1840dfb329' : // 극 귀검술 : 폭풍식
                return this.calculatecalculateFirstAwakeningSkill(levelInfo);
            case '0c262dac3ec41ff79e359ada9c7a7faf' : // 극 귀검술 : 유성락
                return this.calculateGeukGwigeomsulYuseongnak(levelInfo);
            case '2ba299855fc22192cba4f73db75e9d0e' : // 극초발도
                return this.calculateGeukchoBaldo(levelInfo);
            case 'b501ae53638d33a32351904f31cb6aa3' : // 극 귀검술 : 심검
                return this.calculateGeukGwigeomsulSimgeom(levelInfo);
            case 'd8ff976e2aaa4720272a5175d1eb9126' : // 극 발검술 : 섬단
                return this.calculateGeukBalgeomsulSeomdan(levelInfo);
            case '0f638da961acf7958ac6070b7aaed013' : // 이기어검술
                return this.calculateSecondAwakeningSkill(levelInfo);
            case '0113c8b1306ca76d208f83f2d093dd62' : // 극 발검술 : 무형참
                return this.calculateGeukBalgeomsulMuhyeongcham(levelInfo);
            case '8b08f9504167a9c0f3a1d29d71b7943e' : // 천제극섬
                return this.calculateNeoAwakeningSkill(levelInfo);
            // 다른 스킬들도 추가 가능
            default:
                console.error('Unknown skill ID:', skillId);
                return null;
        }
    }

    calculateRiGwigeomsul(levelInfo){
        const baseDamage = levelInfo.optionValue.value1;
        return baseDamage;
    }

    calculateRyusimChung(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1;
        return baseDamage;
    }

    calculateRyusimKae(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1;
        return baseDamage;
    }

    calculateChajiKeuraeshi(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 + levelInfo.optionValue.value2;
    }

    calculateRyusimSeung(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1;
        return baseDamage;
    }

    calculateBaldo(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1;
        return baseDamage;
    }

    calculateMaengryongDangongcham(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 + levelInfo.optionValue.value2;
        return baseDamage;
    }

    calculateChajiBeoseuteu(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 + levelInfo.optionValue.value2 + levelInfo.optionValue.value3;
        return baseDamage;
    }

    calculateHwanyeongGeommu(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 * levelInfo.optionValue.value7 + levelInfo.optionValue.value2 * levelInfo.optionValue.value9;
        return baseDamage;
    }

    calculatecalculateFirstAwakeningSkill(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 + levelInfo.optionValue.value2 * 24 + levelInfo.optionValue.value3;
        return baseDamage;
    }

    calculateGeukGwigeomsulYuseongnak(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 + levelInfo.optionValue.value2 * levelInfo.optionValeu.value3 + levelInfo.optionValue.value5;
        return baseDamage;
    }

    calculateGeukchoBaldo(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 + levelInfo.optionValue.value2;
        return baseDamage;
    }

    calculateGeukGwigeomsulSimgeom(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 * levelInfo.optionValue.value2 + levelInfo.optionValue.value3;
        return baseDamage;
    }

    calculateGeukBalgeomsulSeomdan(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 + levelInfo.optionValue.value2 * levelInfo.optionValue.value3 + levelInfo.optionValue.value4;
        return baseDamage;
    }

    calculateSecondAwakeningSkill(levelInfo) {
        const baseDamage = levelInfo.optionValue.value3 * 14 + levelInfo.optionValue.value4;
        return baseDamage;
    }

    calculateGeukBalgeomsulMuhyeongcham(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 + levelInfo.optionValue.value2 * levelInfo.optionValue.value3 + levelInfo.optionValue.value4;
        return baseDamage;
    }

    calculateNeoAwakeningSkill(levelInfo) {
        const baseDamage = levelInfo.optionValue.value1 + levelInfo.optionValue.value2 + levelInfo.optionValue.value3 * levelInfo.optionValue.value4 +
        levelInfo.optionValue.value5 * levelInfo.optionValue.value6 + levelInfo.optionValue.value7;
        return baseDamage;
    }
}