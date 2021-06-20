<template>

</template>

<script>
// const global =


import router from "../router";
import axios from "axios";
export default {
  //
  name: "Global",
  // global
  install(Vue) {
    // Vue.prototype.$global = {
    //   me:null,
    //   baseUrl: 'http://localhost:8099/'
    // }
    Vue.prototype.$utils = { //全局方法
       setStorage: function (key, value) {
         if(value != null) {
           sessionStorage.setItem(key, JSON.stringify(value));
         }
       },
      getStorage:function (key) {
        const obj = sessionStorage.getItem(key);
        if (obj == null) {
          return null;
        }
        return JSON.parse(obj);
      },
      popStorage: function (key) {
         const res = this.getStorage(key);
         if (res != null) {
           sessionStorage.removeItem(key);
         }
         return res;
      },
      currentUser: function () {
        return this.getStorage("me");
      },
      getAndSetUserInfo: function (userID, setUserInfo) {
         //setUserInfo is a function with param 'userInfo'
        axios.get("/api/user/" + userID).then((res) => {
          if (res.data.success) {
            const userInfo = res.data.data;
            setUserInfo(userInfo);
          }
        })
      },
      baseUrl: function () {
         return "localhost:8080/"
      },
      copyToBoard: function (data) {
        const input = document.createElement("input");     // 直接构建input
        input.value = data;   // 设置内容
        document.body.appendChild(input);        // 添加临时实例
        input.select();      // 选择实例内容
        document.execCommand("Copy");     // 执行复制
        document.body.removeChild(input);
      },
      alertAnd2Login:function () {
         window.alert("请先登录！");
         router.push("/login");
      },
      refreshUser: function (hook) {
        axios.get("/api/user/me").then((res) => {
          console.log("refresh", res)
          if (res != null && res.data.success) {
            // console.log("push", res.data.length)
            this.setStorage("me", res.data.data)
            if (hook != null) {
              hook()
            }
          } else {
            console.log("pop")
            this.popStorage("me")
          }

          if (this.currentUser() == null) {
            this.alertAnd2Login();
          }
        });
      },
      formatSize: function (size) {
         const types = ["B", "KB", "MB", "GB", "TB"]
        let left = size % 1024;
        let flag = 0;
        size /= 1024;
        while (size > 1) {
          flag++;
          left = size % 1024;
          size /= 1024;
        }
        return left.toFixed(2) + types[flag]
      }
    }
  }
}
</script>

<style scoped>

</style>
