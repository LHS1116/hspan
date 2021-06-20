<template>
  <div>
  <el-row>
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
      <el-menu-item index="1">文件列表</el-menu-item>
      <el-submenu index="2">
        <template slot="title">我的工作台</template>
        <el-menu-item index="2-1">文件</el-menu-item>
        <el-menu-item index="2-2">收藏</el-menu-item>
        <el-menu-item index="2-3">选项3</el-menu-item>
        <el-submenu index="2-4">
          <template slot="title">选项4</template>
          <el-menu-item index="2-4-1">选项1</el-menu-item>
          <el-menu-item index="2-4-2">选项2</el-menu-item>
          <el-menu-item index="2-4-3">选项3</el-menu-item>
        </el-submenu>
      </el-submenu>
      <el-menu-item index="3" disabled>消息中心</el-menu-item>

      <div>
        <el-button @click="userInfo" circle size="small" style="float: right; border: white" >
          <el-avatar >
            <span id="username" style="font-size: small" v-text=getUserName()></span>
          </el-avatar>
        </el-button>


      </div>
      <el-button v-if="isLogin()" style="float: right; margin-right: 5px; margin-bottom: 0; display: inline-block" @click="logout">注销</el-button>
    </el-menu>
  </el-row>
  </div>
</template>

<script>


import route from "../router";
import Vue from "vue";

export default {
  name: "NavBarTop",
  data() {
    return {
      activeIndex: '1'
    };
  },
  created() {
    Vue.prototype.navBar = this;
  },
  methods: {
    getUserName: function (){
      const currentUser = this.$utils.currentUser();
      console.log("navbartop", currentUser)

      if (currentUser === null) {
        return "未登录";
      }
      return currentUser.username;
    },
    userInfo: function (){
      const currentUser = this.$utils.currentUser();

      if (currentUser === null) {
        this.$router.push(
          {
            path:"/login"
          }
        )
      } else {
        this.$router.push(
          {
            path:"/my/files"
          }
        )
      }
    },
    isLogin: function () {
      return this.$utils.currentUser() != null;
    },
    logout: function () {
      this.axios.get("/api/login/out").then((res) => {
        this.$utils.popStorage("me", res.data)
        this.$forceUpdate();
        route.push("/login")
      });
    }
  }
}
</script>

<style scoped>

</style>
