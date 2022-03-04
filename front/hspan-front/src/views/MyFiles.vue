<template>
  <div>
    <el-upload
      class="upload-demo"
      ref="upload"
      :action="uploadUrl()"
      :on-success="this.onUploadSuccess"
      :on-error="()=>{alert('上传失败')}"
      :file-list="uploadFiles"
      :auto-upload="false"
      :data="fileUploadData"
      :on-change="onChange"
      >
      <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传</el-button>
      <template #tip>
        <div class="el-upload__tip">
          单文件大小不能超过10GB
        </div>
      </template>
    </el-upload>
    <el-table
      :data="fileList"
      stripe
      style="width: 100%">
      <el-table-column
        prop="num"
        label="序号"
        width="180">
      </el-table-column>
      <el-table-column
        prop="name"
        label="文件名"
        width="180">
      </el-table-column>
      <el-table-column
        prop="uploadDate"
        label="时间">
      </el-table-column>
      <el-table-column
        prop="size"
        label="大小">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template #default="scope">
          <el-button @click="downloadFile(scope.$index)" type="text" size="small">
            <i class="el-icon-download"></i>
          </el-button>
          <el-popover
            placement="top"
            width="160"
            v-model="visible"
            style="text-align: center">
            <div name="shareDiv">
              <el-radio v-model="radio" style="display: inline-block" label="public"> 公开</el-radio>
              <el-radio v-model="radio" style="display: inline-block" label="private"> 私密</el-radio>
            </div>
            <el-button type="primary" style="margin-top: 10px; text-align: center" size="mini" @click="createShare(scope.$index)">创建分享链接</el-button>
            <el-button type="text" size="small" slot="reference" >
              <i class="el-icon-share"></i>
            </el-button>
          </el-popover>
          <el-button type="text" size="small" @click="deleteFile(scope.$index)">
            <i class="el-icon-delete"></i>
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import Vue from "vue"
export default {
  name: "MyFiles",
  data() {
    return {
      radio:'public',
      fileList:[],
      uploadFiles:[],
      fileUploadData: {
        names:[],
        name:""
      }
    }
  },
  mounted() {
    this.listFile();
  },
  methods: {

    listFile: function () {

      this.fileList = [];
      const currentUser = this.$utils.currentUser();
      if (currentUser != null) {
        const fileIDList = currentUser.files;
        console.log("idlist:", fileIDList)
        for (const fileID in fileIDList) {
          this.getFileInfo(fileID);
        }
        console.log("files", this.fileList)
      } else {
        this.$router.push("/login")
      }
    },
    getFileInfo: function (fileID) {
      this.$axios.get("/api/file/info/" + fileID).then((res) => {
        console.log("getFileInfo", res)
        if (res.data.success) {
          res.data.data.size = this.$utils.formatSize(res.data.data.size);
          res.data.data.num = this.fileList.length + 1;
          this.fileList.push(res.data.data)
        }
        this.fileList
        // alert(res.data.status);
      })

    },
    downloadFile: function (index) {
      const fileID = this.fileList[index].id;
      const downloadURL = "/api/file/download/" + fileID;
      window.open(downloadURL);

    },
    deleteFile: function (index) {
      console.log(this.fileList)
      const fileID = this.fileList[index].id;
      this.$axios.delete("/api/file/" + fileID).then((res) => {
        if (!res.data.success) {
          alert(res.data.status);
        } else {
          this.$utils.refreshUser(this.listFile);
        }
      })
    },
    uploadUrl: function () {
      return "/api/file/upload/";
    },
    onChange: function (file, fileList) {
      console.log(file.name)
      this.fileUploadData.names.push(file.name);
      this.fileUploadData.name = file.name;
    },

    submitUpload: function() {
      this.$refs.upload.submit();
    },
    onUploadSuccess: function (response, file, fileList) {
      const fileID = response.id;
      alert("上传成功");
      this.$utils.refreshUser(this.listFile);
      // setTimeout(() => {
      //   this.listFile();
      // }, 300)
    },
    createShare: function (index) {
      const file = this.fileList[index];
      const currentUser = this.$utils.currentUser();
      if (currentUser == null) {
        this.$utils.alertAnd2Login();
      }
      const shareModel = {
        fromID: currentUser.id,
        toID: null,
        fileID:file.id,
        isPublic: true
      }
      if (this.radio === 'private') {
        shareModel.isPublic = false;
      }
      this.$axios
        .post("/api/share/", shareModel)
        .then((res) => {
          // console.log(res);
          let success = res.data.success;
          if(success) {
            const shareUrl = this.$utils.baseUrl() + "file/" + file.id + "/" + res.data.id;
            const code = res.data.data;
            console.log(shareUrl)
            this.$utils.copyToBoard(shareUrl);
            let msg = "分享链接已复制到剪贴板\n链接：" + shareUrl;
            if (this.radio === 'private') {
              msg += "\n提取码为：" + code;
            }
            alert(msg);
            // this.$router.push("/my/files")
          } else {
            alert(res.data.data)
          }
        })
    }
  }
}
</script>

<style scoped>

</style>
