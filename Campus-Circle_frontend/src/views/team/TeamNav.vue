<template>
<div id="team">
  <div class="left-list">
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
  <div class="article-list">
    <el-tabs v-model="activeName" class="article-tabs" @tab-click="handleClick">
      <el-tab-pane label="推荐" name="first"><ArticleComponent/></el-tab-pane>
      <el-tab-pane label="最新" name="second">Config</el-tab-pane>
    </el-tabs>
  </div>
  <div class="right-list">
    <p>紧急招募</p>
  </div>
</div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'
import ArticleComponent from '@/views/article/ArticleList.vue'
const activeName = ref('first')

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}
</script>


<style scoped>
#team {
  display: flex; /* 使用 Flexbox  div左右分布 */
  justify-content: center; /* 水平居中对齐 */
  margin: 50px;
}
.left-list {
  width: 150px;
  height: 300px;
  background-color: rgb(255, 248, 248); /* 背景颜色 */
  border: 1px solid #eaeaea; /* 边框 */
  margin-right: 10px;
  padding: 0; /* 去掉内边距 */
  display: flex; /* 使用 Flexbox */
}
.article-list {
  width: 850px;
  margin:0 50px 0 50px;
  padding: 0 50px 30px 50px;
  border-radius: 3px;
  border: 1px solid #eaeaea; /* 边框 */
}
.right-list {
  width: 150px;
  height: 300px;
  justify-content: center; /* 水平居中对齐 */
  border: 1px solid #eaeaea; /* 边框 */
  margin-right: 10px;
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

.article-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>
