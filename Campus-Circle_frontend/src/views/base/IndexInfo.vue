<template>
  <div id="content">
    <div class="base">
      <label for="avatar">
        <el-avatar :size="80" :src="user?.avatarUrl" alt="头像" class="clickable-avatar" />
      </label>
      <div class="username">
        <span class="name">{{ user?.username || '未设置用户名' }}</span>
        <el-button round>编辑资料</el-button>
        <el-button round>文章管理</el-button>
        <el-button round>设置</el-button>
        <div class="info-container">
          <span class="info">
            <span class="info-title">文章: </span>
            <span class="info-content">0 </span>
          </span>
          <span class="fans">
            <span class="info-title">粉丝: </span>
            <span class="info-content">0 </span>
          </span>
        </div>
      </div>
    </div>
    <div class="body">
    <!-- 左侧菜单区域 -->
    <div class="left-container">
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

<!--     右侧区域-->
    <div class="right-container">

      <el-tabs type="border-card" class="right-tabs">
        <el-tab-pane label="文章">文章</el-tab-pane>
        <el-tab-pane label="队伍">队伍</el-tab-pane>
        <el-tab-pane label="我的关注">我的关注</el-tab-pane>
        <el-tab-pane>

          <template #label>
            <div class="search">
              <el-input
                v-model="search"
                style="max-width: 600px"
                placeholder="Please input"
                class="input-with-select"
              >
                <template #append>
                  <el-button class="search-button">
                    <el-icon><Search/></el-icon>搜索
                  </el-button>
                </template>
              </el-input>
            </div>
          </template>

        </el-tab-pane>
      </el-tabs>

    </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/counter'
import { getUserInfo } from '@/service/axios/user'
import type { UserLoginRes } from '@/model/UserVO'
import { onMounted, ref } from 'vue'
import { Search } from '@element-plus/icons-vue'

const user = ref<UserLoginRes | null>(null)

const userStore = useUserStore()
const userId = userStore.userId

onMounted(async () => {
  if (!userId) {
    ElMessage.error('用户ID为空，无法获取用户信息')
    return
  }
  try {
    const response = await getUserInfo(userId.toString())
    if (response?.data?.data) {
      const userData = response.data.data
      user.value = userData
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败:', error)
  }
})


</script>

<style scoped>
#content {
}
.search-button {
  display: flex;
  align-items: center;
  justify-content: center;
}

.base {
  display: flex; /* 使用 Flexbox  div左右分布 */
  align-items: center; /* 容器 垂直居中对齐 */
  justify-content: center; /* 水平居中对齐 */
  margin: 20px;
  padding: 20px;
}

.name {
  font-weight: bold; /* 仅针对 class 为 name 的 span 字体加粗 */
  margin: 20px;
}
.info-container {
  margin: 10px 0 0 20px;
}

.body {
  display: flex;
  justify-content: center; /* 水平居中对齐 */
  margin: 20px;
}

.left-container {
  width: 200px; /* 调整宽度 */
  background-color: rgb(255, 248, 248); /* 背景颜色 */
  border: 1px solid #eaeaea; /* 边框 */
  margin-right: 10px;
  padding: 0; /* 去掉内边距 */
  display: flex; /* 使用 Flexbox */
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
.right-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
.right-tabs .custom-tabs-label .el-icon {
  vertical-align: middle;
}
.right-tabs .custom-tabs-label span {
  vertical-align: middle;
  margin-left: 4px;
}
</style>
