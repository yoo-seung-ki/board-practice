const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})


module.exports = {
  devServer: {
    port: 8081,  // 원하는 포트 번호로 변경
    proxy: {
     '/api': {
       target: 'https://api.neople.co.kr',
       changeOrigin: true,
       pathRewrite: { '^/api': '' },
     },
    },
  }
}
