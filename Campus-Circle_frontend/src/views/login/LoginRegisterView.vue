<template>
  <div class="auth-container">
    <!-- 显示注册成功提示 -->
    <el-alert
      v-if="showSuccessAlert"
      title="注册成功"
      type="success"
      effect="dark"
      :closable="false"
    />
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="登录" name="login">
        <div class="form-container">
          <el-form :model="loginForm" label-width="100px">
            <el-form-item label="账号" :rules="loginRules.userAccount">
              <el-input clearable v-model="loginForm.userAccount" />
            </el-form-item>
            <el-form-item label="密码" :rules="loginRules.userPassword">
              <el-input show-password type="password" v-model="loginForm.userPassword" />
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="rememberMe">记住密码</el-checkbox>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleLogin">登录</el-button>
              <el-button @click="handleForgotPassword">忘记密码?</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>
      <el-tab-pane label="注册" name="register">
        <div class="form-container">
          <el-form :model="registerForm" label-width="100px">
            <el-form-item label="邮箱" :rules="registerRules.email">
              <el-input clearable v-model="registerForm.email" />
            </el-form-item>
            <el-form-item label="账号" :rules="registerRules.userAccount">
              <el-input clearable v-model="registerForm.userAccount" />
            </el-form-item>
            <el-form-item label="密码" :rules="registerRules.userPassword">
              <el-input show-password type="password" v-model="registerForm.userPassword" />
            </el-form-item>
            <el-form-item label="确认密码" :rules="registerRules.checkPassword">
              <el-input show-password type="password" v-model="registerForm.checkPassword" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleRegister">注册</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { UserLoginParams, UserRegisterParams } from '@/model/LoginRegisterParams'
import { userLogin, userRegister } from '@/service/axios/user'
import { useUserStore } from '@/stores/counter'

const activeTab = ref('login')
const rememberMe = ref(false)
const userStore = useUserStore();

// 定义loginForm和registerForm类型
const loginForm = ref<UserLoginParams>({
  userAccount: '',
  userPassword: ''
})

const registerForm = ref<UserRegisterParams>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
  email: '' // 添加email字段
})

// 确认密码自定义验证
// eslint-disable-next-line @typescript-eslint/no-unsafe-function-type
const checkPassword = (rule: never, value: string, callback: Function) => {
  if (value !== registerForm.value.userPassword) {
    callback(new Error('确认密码与密码不一致'))
  } else {
    callback()
  }
}

// 登录表单验证规则
const loginRules = {
  userAccount: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 注册表单验证规则
const registerRules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, message: '账号长度不能少于3位', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: checkPassword, trigger: 'blur' }
  ]
}

const handleTabClick = () => {
  console.log(`切换标签`)
}


const handleLogin = async () => {
  const { userAccount, userPassword } = loginForm.value

  // 校验账号和密码是否为空
  if (!userAccount || !userPassword) {
    console.error('请输入账号和密码')
    return
  }

  try {
    // 调用登录接口
    const response = await userLogin(userAccount, userPassword)
    if (response?.data?.code === 200) {
      console.log('登录成功:', response?.data)
      userStore.setUser(response?.data.data.id, response?.data.data.userRole)
      // 登录成功后，判断是否勾选了记住密码
      if (rememberMe.value) {
        // 记住密码，保存账号和密码
        localStorage.setItem('userAccount', userAccount)
        localStorage.setItem('userPassword', userPassword)
      } else {
        // 不记住密码，清除之前保存的信息
        localStorage.removeItem('userAccount')
        localStorage.removeItem('userPassword')
      }
      // 登录成功后保存token或跳转
      //localStorage.setItem('token', response?.data?.token) // 假设返回的token在data中

      // 刷新当前页面
      window.location.reload();
    } else {
      console.log('登录失败:', response?.data)
    }
  } catch (error) {
    console.error('登录失败:', error)
  }
}

// 控制成功提示显示的状态
const showSuccessAlert = ref(false)
const handleRegister = async () => {
  const { userAccount, userPassword, checkPassword, email } = registerForm.value

  // 确保确认密码和密码一致
  if (userPassword !== checkPassword) {
    console.error('密码和确认密码不一致')
    return
  }

  try {
    const response = await userRegister(
      userAccount || '',
      userPassword || '',
      checkPassword || '',
      email || ''
    )
    if (response?.data?.code === 200) {
      console.log('注册成功:', response)
      // 显示成功提示
      showSuccessAlert.value = true

      // 清除注册表单中的数据
      registerForm.value.userAccount = ''
      registerForm.value.userPassword = ''
      registerForm.value.checkPassword = ''
      registerForm.value.email = ''

      // 2秒后隐藏提示
      setTimeout(() => {
        showSuccessAlert.value = false;
      }, 2000);
      // 切换到登录标签
      setTimeout(() => {
        activeTab.value = 'login'
      }, 1500) // 延迟1.5秒切换标签，给用户一些时间看到提示
    } else {
      console.log('注册失败:', response?.data?.code)
      // 处理注册失败逻辑
    }
  } catch (error) {
    console.error('注册过程中发生错误:', error)
    // 处理请求错误
  }
}

const handleForgotPassword = () => {
  const userAccount = loginForm.value.userAccount
  if (userAccount) {
    console.log('处理忘记密码，发送邮件至:', userAccount)
    // 处理忘记密码逻辑
  } else {
    console.log('请先输入邮箱')
  }
}

// 在组件挂载时，检查是否有已保存的登录信息
onMounted(() => {
  const savedAccount = localStorage.getItem('userAccount')
  const savedPassword = localStorage.getItem('userPassword')
  if (savedAccount) {
    loginForm.value.userAccount = savedAccount
  }

  if (savedPassword) {
    loginForm.value.userPassword = savedPassword
    rememberMe.value = true // 如果有密码，则默认选中记住密码
  }
})
</script>



<style scoped>
.auth-container {
  max-width: 400px;
  margin: 50px auto;
}

.form-container {
  height: 275px; /* 设置固定高度 */
  width: 320px;
  overflow: auto; /* 内容溢出时滚动 */
}
</style>
