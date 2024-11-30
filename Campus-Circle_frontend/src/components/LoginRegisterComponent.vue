<template>
  <div class="login-register-component">
    <div class="modal-content">
      <div class="close-icon-wrapper">
        <el-icon class="close-icon" @click="handleClose">
          <img src="@/assets/svg/close.svg" alt="关闭" />
        </el-icon>
      </div>
      <LoginRegisterView />
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineEmits } from 'vue';
import LoginRegisterView from '@/views/login/LoginRegisterView.vue'; // 替换为你的组件路径

const emit = defineEmits<{
  (event: 'close'): void;
}>();

// 关闭事件的处理方法
const handleClose = () => {
  emit('close');  // 触发父组件的 'close' 事件
};
</script>

<style scoped>
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  position: relative;
}

.close-icon-wrapper {
  position: absolute;
  top: 10px;
  right: 10px;
}

.close-icon {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.close-icon:hover {
  opacity: 0.8;
}

img {
  width: 25px;
  height: 25px;
}
</style>
