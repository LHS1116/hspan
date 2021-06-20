<template>
<div id="userInfo">
  <el-container>
    <el-col :span="2"></el-col>
    <el-col :span="20">
      <el-row id="avatar">
        <el-avatar :size=100  style="text-align: center"  >
          <span style="font-size: large" v-text=getUserName()></span>
        </el-avatar>
      </el-row>
      <el-row id="storage_percentage">
        <el-col :span="10" offset=7>
          <el-progress  :stroke-width="20" :percentage=getUsedStorage(true)></el-progress>
        </el-col>
      </el-row>
      <el-divider></el-divider>
      <router-view></router-view>
    </el-col>
    <el-col :span="2"></el-col>
  </el-container>
</div>
</template>

<script>
import Vue from "vue";

export default {
  name: "UserInfo",
  methods: {
    getUserName: function (){
      const currentUser = this.$utils.currentUser();
      console.log("userinfo", currentUser)
      if (currentUser === null) {
        return "未登录";
      }
      return currentUser.username;
    },
    getUsedStorage: function (flag) {
      // const currentUser = this.$global.me;
      const currentUser = this.$utils.currentUser();

      const used = currentUser === null ? 0 : 0;
      const total = 10
      if (flag) {
        let percentage = 1;
        if (currentUser != null) {
          percentage =  used / total;
        }
        return percentage;
      }
      return used + " / " + total + "GB";
    }
  }
}
</script>

<style scoped>

</style>
