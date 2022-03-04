<template>
  <div id="app">
    <nav-bar-top v-if="this.success"></nav-bar-top>
    <router-view></router-view>
  </div>




</template>

<script>
import NavBarLeft from "./components/NavBarLeft.vue";
import NavBarTop from "./components/NavBarTop.vue";
import Vue from "vue";
import route from "./router";

export default {
  name: 'app',
  components: {NavBarTop},
  data() {
    return {
      success:false
    }
  },
  created() {

    this.axios.get("/api/user/me").then((res) => {
      console.log("app", res)

      if (res != null && res.data.success) {
        this.$utils.setStorage("me", res.data.data);
        this.success = true;
      } else {
        this.$utils.popStorage("me");
        window.location.href = "http://xsso.lhs.com:8097/login?sourceUrl=" + encodeURIComponent(window.location.href)
      }
    });
  },
  mounted() {

  },
  methods: {

  }

}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
