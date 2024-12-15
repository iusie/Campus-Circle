<template>
  <div id="securityCenter">
    <div class="safe-info">
      <h3>安全中心</h3>
      <el-divider />
      <el-row
        v-for="(item, index) in userInfoList"
        :key="index"
        class="info-row"
      >
        <el-col :span="5">
          <span>{{ item.label }}</span>
        </el-col>
        <el-col :span="16">
          <span>{{ formatValue(item.key) }}</span>
        </el-col>
        <el-divider />
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/counter';
import { getUserInfo } from '@/service/axios/user';
import type { UserLoginRes } from '@/model/UserVO';
import { onMounted, ref } from 'vue';

const userStore = useUserStore();
const userId = userStore.userId;

const user = ref<UserLoginRes | null>(null);

const userInfoList = [
  { label: '手机', key: 'phone' },
  { label: '邮箱', key: 'email' },
];

onMounted(async () => {
  if (userId === null) {
    console.error('用户ID为空，无法获取用户信息');
    return;
  }

  try {
    const response = await getUserInfo(userId.toString());
    user.value = response?.data?.data;
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
});

// 格式化值
const formatValue = (key : any) => {
  if (!user.value) return '';

  switch (key) {
    case 'phone':
      return formatPhone(user.value.phone);
    default:
      return user.value;
  }
};

// 格式化手机号
const formatPhone = (phone : any) => {
  if (!phone || phone.length < 7) return phone; // 确保手机号有效
  return `${phone.slice(0, 3)}****${phone.slice(-4)}`; // 只显示前三位和后四位
};

</script>

<style scoped>

.safe-info {
  margin-top: -18px;
  background: #f9f9f9;
  border-top:  1px solid #E8E8E8;
}

.info-row {
  margin: 10px 0;
}
</style>
