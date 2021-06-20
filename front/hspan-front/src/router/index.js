import Vue from 'vue'
import VueRouter from 'vue-router'
// import HelloWorld from '@/components/HelloWorld'
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import App from "../App";
import UserInfo from "../components/UserInfo";
import MyFiles from "../views/MyFiles";
import FileInfo from "../components/FileInfo";

Vue.use(VueRouter)

const route = new VueRouter({
  mode:'history',
  routes: [
    // {
    //   path:'/',
    //   redirect:{path: '/my/files'}
    // },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {
        title: '登录'
      },
      children: [
        // {
        // path: "/*",
        // redirect:{path: ''}
        // },
        {
          path: '',
          meta:{
            title: "登陆"
          }
        }
      ]
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
      meta: {
        title: '注册'
      }
    },
    {
      path: '/my',
      name: 'UserInfo',
      component: UserInfo,
      meta: {
        title: "个人中心"
      },
      children: [
        {
        path: '',
        redirect: { path: 'files' },
        },
        {
          path: 'files',
          component: MyFiles,
          meta:{title: '我的文件', type: 'files'}
        }
        // {
        //   path: '/favorites',
        //   component: Files,
        //   meta: {title: '我的收藏', type: 'favorites'}
        // },
        // {
        //   path: '/shares',
        //   component: Shares,
        //   meta: {title: '我的分享', type: 'shares'}
        // }

      ]

    },
    {
      path: '/file/:fileID/:shareID',
      name: 'FileInfo',
      component: FileInfo,
      meta: {
        title: '文件详情'
      },
      // children: [
      //   {
      //     path: "/:shareID"
      //   }
      // ]

    },
    {
      path: '*',
      name: 'NotFound',
      redirect: {path: '/my'}
    }

  ]
})
export default route

