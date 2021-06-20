<template>
  <div class="register_container">
    <div class="register_box">
      <h1 class="text-center">注册</h1>

      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerFormRules"
        class="register_form"
      >
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            placeholder="请输入密码"
            prefix-icon="el-icon-view"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item prop="checkPass">
          <el-input
            type="password"
            v-model="registerForm.checkPass"
            autocomplete="off"
            prefix-icon="el-icon-lock"
            placeholder="请再次输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item class="btns" style="text-align: center">
          <el-button type="primary" @click="jumpToLogin">登录</el-button>
          <el-button type="primary" @click="register">注册</el-button>
          <el-button type="info" @click="reSetRegisterForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    const validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.registerForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      registerForm: {
        username: "",
        password: "",
        checkPass: "",
        description: "",
      },
      // 这是表单验证规则对象
      registerFormRules: {
        // 验证用户名是否合法
        username: [
          { required: true, message: "请输入账号", trigger: "change" },
          {
            min: 3,
            max: 16,
            message: "用户名长度在 3 到 16 个字符",
            trigger: "change",
          },
        ],

        //验证密码是否合法
        password: [
          { required: true, message: "请输入密码", trigger: "change" },
          {
            min: 6,
            max: 16,
            message: "密码长度在 6 到 32 个字符",
            trigger: "change",
          },
        ],
        checkPass: [{ validator: validatePass2, trigger: "change" }],
      },
    };
  },
  methods: {
    jumpToLogin() {
      this.$router.push({ path: "/login" });
    },
    // 点击重置按钮，重置登录表单
    reSetRegisterForm() {
      this.$refs.registerFormRef.resetFields();
    },
    register() {
      this.$refs.registerFormRef.validate((valid) => {
        if (valid) {
          this.$axios
            .post("/api/user/register", {
              password: this.registerForm.password,
              username: this.registerForm.username,
            })
            .then((res) => {
              if (!res.data.success) {
                alert(res.data.status)
              } else {
                alert("注册成功!正在跳转至登录页面...");
                setTimeout(() => {
                  this.$router.push({ path: "/login/" });
                }, 500);
              }
            })
            .catch((p) => this.err(p));
        } else {
          console.log("err");
          return false;
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
.register_box {
  width: 320px;
  margin: 0 auto;
}

.btns {
  display: flex;
  justify-content: flex-end;
  // 按钮尾部对齐
}
</style>
