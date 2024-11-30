<template>
  <div class="common-layout-header">
    <el-header id="header">
      <div class="logo">
        <router-link to="/">
          <img src="@/assets/svg/amiya.ico" alt="Logo" />
        </router-link>
      </div>

      <nav class="nav-links">
        <router-link to="/" class="nav-item">主页</router-link>
        <router-link to="/articles" class="nav-item">文章</router-link>
        <router-link to="/team" class="nav-item">组队大厅</router-link>
        <router-link to="/chat" class="nav-item">聊天</router-link>
      </nav>

      <div class="rightLayout">
        <div class="search">
          <el-input
            v-model="search"
            style="max-width: 600px"
            placeholder="Please input"
            class="input-with-select"
          >
            <template #append>
              <el-button class="search-button">
                <img src="@/assets/svg/search.svg" alt="Search" />搜索
              </el-button>
            </template>
          </el-input>
        </div>

        <el-button size="small" type="primary" round>
          <img src="@/assets/svg/edit.svg" alt="Edit" />写文章
        </el-button>
        <el-button size="small" type="primary" round>
          <img src="@/assets/svg/info.svg" alt="info" />消息
        </el-button>

        <el-dropdown class="avatar" @command="handleDropdownCommand">
          <el-avatar :src="avatarSrc" @click="handleAvatarClick" />
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="personalInfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="articleManager">文章管理</el-dropdown-item>
              <el-dropdown-item command="teamManager">队伍管理</el-dropdown-item>
              <el-dropdown-item command="setUp">设置</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <!-- 登录注册组件 -->
    <LoginRegisterComponent v-if="showLoginRegister" @close="closeLoginRegister" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/counter';
import LoginRegisterComponent from '@/components/LoginRegisterComponent.vue'; // 替换为你的组件路径

const router = useRouter();
const userStore = useUserStore();
const avatarSrc = ref('https://jsd.onmicrosoft.cn/gh/iusie/image/unnamed.jpg');
const search = ref('');
const showLoginRegister = ref(false);

// 使用计算属性来获取最新的 loginUserId
const loginUerId = computed(() => userStore.userId);

const handleAvatarClick = () => {
  if (!loginUerId.value) {
    showLoginRegister.value = true; // 显示登录注册组件
  } else {
    router.push('/BaseInfoView');
  }
};

const handleDropdownCommand = (command: string) => {
  if (!loginUerId.value) {
    showLoginRegister.value = true; // 显示登录注册组件
    return;
  }

  switch (command) {
    case 'personalInfo':
      router.push('/personalInfo');
      break;
    case 'articleManager':
      router.push('/articleManager');
      break;
    case 'teamManager':
      router.push('/teamManager');
      break;
    case 'setUp':
      router.push('/setUp');
      break;
    case 'logout':
      router.push('/login');
      break;
  }
};

const closeLoginRegister = () => {
  showLoginRegister.value = false;
};
</script>

<style scoped>
/* 样式保持不变 */
#header {
  background-color: rgba(241, 241, 241, 0.87);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo {
  margin-right: 20px;
}

.logo img {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.nav-links {
  display: flex;
  margin: 0 20px;
}

.nav-item {
  margin: 0 15px;
  text-decoration: none;
  color: #333;
}

.nav-item:hover {
  text-decoration: underline;
  color: #626aef;
}

.rightLayout {
  display: flex;
  align-items: center;
}

.search {
  display: flex;
  align-items: center;
  margin-right: 150px;
}

.search-button {
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  margin-left: 10px;
}

.rightLayout img {
  width: 20px;
  height: 20px;
  margin-right: 5px;
}

/* 登录注册组件样式 */
.login-register-component {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 2; /* 确保在最上层 */
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
