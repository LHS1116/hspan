// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui';
import './plugin/axios'
import moment from 'moment'
import router from './router'
import 'element-ui/lib/theme-chalk/index.css';
import Global from "./components/Global";
Vue.use(ElementUI);
Vue.use(Global)

// Vue.use(VueRouter)

Vue.config.productionTip = false

//当前用户信息
// Vue.prototype.$global = {
//   me: null,
//   baseUrl: 'localhost:8099/',
// }
// Vue.prototype.global = Global.global

//
// Vue.prototype.isLogin = function () {
//   return this.global.me == null;
// }





/* eslint-disable no-new */
// new Vue({
//   el: '#app',
//   components: { App },
//   template: '<app/>',
//   // router: router
//   // el: '#app',
//   // router,
//   // render: h => h(App)
// })
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
