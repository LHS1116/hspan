module.exports = {
  // publicPath: '/',
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8099',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api'
        }
      }
    },
    disableHostCheck: true
  }
}
