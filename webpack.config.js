const path = require('path');

module.exports = {
  entry: './src/main/js/index.js', // 시작점 파일
  output: {
    path: path.resolve(__dirname, 'src/main/resources/static'), // 번들 파일의 출력 경로
    filename: 'bundle.js' // 번들 파일 이름
  },
  // 필요한 로더 및 플러그인 설정 추가
};