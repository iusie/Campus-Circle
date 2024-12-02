<template>
  <div>
    <HeaderView />
    <!-- 用户信息区域 -->
    <div class="user-info">
      <!-- 左侧菜单区域 -->
      <div class="menu-container">
        <el-menu default-active="1" class="menu" vertical>
          <el-menu-item index="1">
            <router-link to="/userInfo/personInfo" class="nav-item">个人中心</router-link>
          </el-menu-item>
          <el-menu-item index="2">
            <router-link to="/userInfo/securityCenter" class="nav-item">安全中心</router-link>
          </el-menu-item>
          <el-menu-item index="3">
            <router-link to="#" class="nav-item">文章管理</router-link>
          </el-menu-item>
          <el-menu-item index="4">
            <router-link to="#" class="nav-item">队伍管理</router-link>
          </el-menu-item>
          <el-menu-item index="5">
            <router-link to="#" class="nav-item">设置</router-link>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 右侧头像和按钮区域 -->
      <div class="avatar-container">
          <router-view />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import HeaderView from '@/components/HeaderView.vue'
import { onMounted, ref } from 'vue'
import { getUserInfo } from '@/service/axios/user'
import type { UserLoginRes } from '@/model/UserVO'
import { useUserStore } from '@/stores/counter'

const userStore = useUserStore();
const userId = userStore.userId;

const user = ref<UserLoginRes | null>(null) // 修改为单个用户对象或 null

onMounted(async () => {
  if (userId === null) {
    console.error('用户ID为空，无法获取用户信息');
    return;
  }

  try {
    // 将 BigInt 类型的 userId 转换为字符串
    const response = await getUserInfo(userId.toString())
    user.value = response?.data?.data // 从 AxiosResponse 中提取数据
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
})
</script>


<style scoped>
.user-info {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: flex-start; /* 不需要垂直居中 */
  background-color: #ffffff;
  border-radius: 8px;
  width: 100%;
  height: 100%;
  margin-top: 80px;
}

.menu-container {
  width: 200px; /* 调整宽度 */
  background-color: rgb(255, 248, 248); /* 背景颜色 */
  border: 1px solid #eaeaea; /* 边框 */
  margin-right: 10px;
  padding: 0; /* 去掉内边距 */
  display: flex; /* 使用 Flexbox */
}

.avatar-container {
  width: 45%;
  padding: 20px; /* 内边距 */
}


.menu {
  width: 100%; /* 改为100%以适应容器 */
}

.el-menu-item {
  text-align: center;
  width: 100%; /* 让菜单项占满宽度 */
  list-style-type: none; /* 隐藏小圆点 */
  padding: 0; /* 去掉内边距 */
}

.el-menu-item .nav-item {
  color: black; /* 字体颜色设置为黑色 */
  text-decoration: none;
  font-weight: 500; /* 字体加粗 */
  transition: color 0.3s; /* 添加过渡效果 */
  display: block; /* 使链接占据整个菜单项 */
  padding: 10px; /* 内边距，增加可点击区域 */
}

/* 鼠标悬停时的背景颜色 */
.el-menu-item:hover .nav-item {
  color: black; /* 保持字体颜色为黑色 */
}

.el-menu-item:active .nav-item {
  color: #3a8ee6; /* 点击效果 */
}
</style>
