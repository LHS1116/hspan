<template>
  <div>
    <div v-if="access === 'private'" name="preview">
      <el-form ref="form" style="display: inline-block">
        提取码：<el-input
          placeholder="请输入提取码"
          v-model="code"
          clearable>
        </el-input>
        <el-button type="primary" @click="checkCode">提交</el-button>
      </el-form>

    </div>
    <div v-if="access === 'public'">
      <div name="fileInfo">
        <i class="el-icon-document" style="font-size: 218px"></i><br>
        <span style="font-size: 33px">{{fileInfo.name}}</span><br>
        <span style="font-size: 30px">大小：{{fileInfo.size}}</span><br>
        <span style="font-size: 30px">所有者：{{this.fileOwner}}</span><br>
      </div>
      <div id="file_operation">
        <el-button type="success" @click="reSave">
          <i class="el-icon-star-off" style="font-weight: bold">保存到我的网盘</i>
        </el-button>
        <el-button type="primary" @click="downloadFile">
          <i class="el-icon-download" style="font-weight: bold">下载</i>
        </el-button>
        <el-button>
          <i class="el-icon-share" style="font-weight: bold">分享</i>
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FileInfo",
  data() {
    return {
      fileInfo: {},
      access: "",
      fileOwner: "",
      code:"",
    }
  },
  created() {
    const shareID = this.$route.params.shareID;
    this.getShareInfo(shareID);
  },
  methods: {
    getFileInfo: function (fileID) {
      this.$axios.get("/api/file/info/" + fileID).then((res) => {
        console.log("getFileInfo", res)
        if (res.data.success) {
          res.data.data.size = this.$utils.formatSize(res.data.data.size);
          this.fileInfo = res.data.data;
        }
        // alert(res.data.status);
      })

    },
    getShareInfo: function (shareID) {
      this.$axios.get("/api/share/" + shareID).then((res) => {
        console.log("getShareInfo", res)
        if (res.data.success) {

          this.$utils.getAndSetUserInfo(res.data.data.fromID, this.setOwnerInfo);
          if (res.data.data.isPublic) {
            this.getFileInfo(this.$route.params.fileID)
          }
          this.access = res.data.data.isPublic ? "public" : "private";
        } else {
          alert("错误的分享链接！")
        }
        // alert(res.data.status);
      })
    },
    setOwnerInfo: function (userInfo) {
      this.fileOwner = userInfo.username;
    },
    formatSize() {
      let size = this.fileInfo.size;
      return this.$utils.formatSize(size);
    },
    checkCode() {
      const code = this.code;
      this.$axios.get("/api/share/check/" + this.$route.params.shareID + "?code=" + code).then((res) => {
        if (!res.data.success) {
          alert(res.data.status);
        } else {
          this.getFileInfo(this.$route.params.fileID)
          this.access = "public";
        }
      })
    },
    downloadFile: function () {
      const fileID = this.fileInfo.id;
      const downloadURL = "/api/file/download/" + fileID;
      window.open(downloadURL);

    },
    reSave() {
      const currentUser = this.$utils.currentUser();
      const fileID = this.fileInfo.id;
      this.$axios.get("/api/file/save/" + fileID).then((res) => {
        if (!res.data.success) {
          alert(res.data.status);
        } else {
          alert("保存成功！")
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
