<template>
  <div>
    <h1>5x Slow Motion Video</h1>
    <video ref="videoPlayer" width="640" height="360" controls>
      <source :src="videoSource" type="video/mp4" />
      Your browser does not support the video tag.
    </video>
  </div>
</template>

<script>
export default {
  data() {
    return {
      videoSource: require('@/assets/gwangryong.mp4'), // 비디오 파일 경로
    };
  },
  mounted() {
    // 비디오 재생 속도 설정
    this.$refs.videoPlayer.playbackRate = 0.2;
  },
};
</script>

<style scoped>
/* 추가적인 스타일이 필요하다면 여기에 작성하세요 */
</style>
