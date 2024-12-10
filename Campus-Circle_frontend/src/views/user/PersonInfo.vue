<template>
  <div id="personInfo">
    <div class="avatar-section">
      <label for="avatar-upload">
        <el-avatar :size="100" :src="user?.avatarUrl" alt="头像" class="clickable-avatar" />
      </label>
      <input
        id="avatar-upload"
        type="file"
        accept="image/*"
        style="display: none;"
        @change="handleAvatarChange"
      />
      <div class="username-container">
        <span v-if="!isEditing">{{ user?.username || '未设置用户名' }}</span>
        <el-input
          v-else
          v-model="user.username"
          placeholder="请输入用户名"
          class="username-input"
          size="small"
        />
      </div>
    </div>
    <div class="info-section">
      <h3>个人信息</h3>
      <el-divider />
      <el-row
        v-for="(item, index) in userInfoList"
        :key="index"
        class="info-row"
      >
        <el-col :span="5">
          <span>{{ item.label }}：</span>
        </el-col>
        <el-col :span="16">
          <span v-if="!isEditing && item.key !== 'tags' || item.key === 'userAccount' || item.key === 'createTime'">
            {{ formatValue(item.key) }}
          </span>
          <div v-else-if="!isEditing && item.key === 'tags'" class="tags-container">
            <el-tag
              v-for="(tag, tagIndex) in formattedTags"
              :key="tagIndex"
              :style="{ marginRight: '5px', marginBottom: '5px' }"
              :type="randomTagType(tagIndex)"
            >
              {{ tag }}
            </el-tag>
          </div>
          <div v-else-if="item.key === 'gender'">
            <el-radio-group v-model="user[item.key]">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
            </el-radio-group>
          </div>
          <div v-else-if="item.key !== 'tags'">
            <el-input v-model="user[item.key]" placeholder="请输入{{ item.label }}" />
          </div>
          <div v-else>
            <div class="tags-container">
              <el-tag
                v-for="(tag, tagIndex) in formattedTags"
                :key="tagIndex"
                closable
                :style="{ marginRight: '5px', marginBottom: '5px' }"
                :type="randomTagType(tagIndex)"
                @close="removeTag(tagIndex)"
              >
                {{ tag }}
              </el-tag>
              <el-input
                v-if="isEditing"
                v-model="newTag"
                placeholder="添加标签"
                @keyup.enter="addTag"
                style="width: 100px;"
              />
            </div>
          </div>
        </el-col>
      </el-row>
      <el-button v-if="!isEditing" type="primary" @click="isEditing = true">编辑</el-button>
      <el-button v-if="isEditing" type="success" @click="updateUser">保存</el-button>
      <el-button v-if="isEditing" @click="isEditing = false">取消</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/counter';
import { getUserInfo, updateUserInfo } from '@/service/axios/user';
import type { UserLoginRes } from '@/model/UserVO';
import { onMounted, ref, computed } from 'vue';

const userStore = useUserStore();
const userId = userStore.userId;

const user = ref<UserLoginRes | null>(null);
const isEditing = ref(false);
const newTag = ref('');

const userInfoList = [
  { label: '账户', key: 'userAccount' },
  { label: '自我介绍', key: 'userProfile' },
  { label: '性别', key: 'gender' },
  { label: '标签', key: 'tags' },
  { label: '创建时间', key: 'createTime' },
];

// 标签数据解析函数
const parseTags = (tags: any): string[] => {
  if (!tags) return [];
  if (Array.isArray(tags)) {
    return tags; // 已经是数组
  }
  try {
    if (typeof tags === 'string') {
      return JSON.parse(tags); // 尝试解析 JSON 字符串
    }
    if (typeof tags === 'object') {
      return Object.values(tags); // 将 Proxy 或对象转为数组
    }
  } catch (e) {
    console.error('解析标签失败:', e);
  }
  return [];
};

// 格式化标签
const formattedTags = computed({
  get() {
    return parseTags(user.value?.tags); // 使用解析函数
  },
  set(tags: string[]) {
    if (user.value) {
      user.value.tags = JSON.stringify(tags); // 保存为字符串
    }
  },
});

// 随机标签类型
const tagTypes = ['success', 'info', 'warning', 'danger'];
const randomTagType = (index) => tagTypes[index % tagTypes.length];

onMounted(async () => {
  if (!userId) {
    console.error('用户ID为空，无法获取用户信息');
    return;
  }
  try {
    const response = await getUserInfo(userId.toString());
    if (response?.data?.data) {
      const userData = response.data.data;
      userData.tags = parseTags(userData.tags); // 确保 tags 为数组
      user.value = userData;
      console.log('获取用户信息成功:', user.value);
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
});

// 格式化值
const formatValue = (key) => {
  if (!user.value) return '';

  switch (key) {
    case 'gender':
      return user.value.gender === 1 ? '男' : '女';
    case 'createTime':
      return formatDate(user.value.createTime);
    default:
      return user.value[key] || '无信息';
  }
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toISOString().split('T')[0];
};

// 上传头像
const handleAvatarChange = async (event) => {
  const file = event.target.files[0];
  if (!file) {
    ElMessage.error('请选择一张图片');
    return;
  }

  const formData = new FormData();
  formData.append('file', file);

  try {
    const response = await uploadAvatarAPI(formData);
    if (response?.data?.success) {
      user.value.avatarUrl = response.data.url;
      ElMessage.success('头像上传成功');
    } else {
      ElMessage.error('头像上传失败');
    }
  } catch (error) {
    ElMessage.error('上传失败: ' + error.message);
  }
};

// 上传头像 API 示例
const uploadAvatarAPI = async (formData) => {
  // 根据实际情况替换上传逻辑
};

// 添加标签
const addTag = () => {
  const trimmedTag = newTag.value.trim();
  if (trimmedTag && !formattedTags.value.includes(trimmedTag)) {
    formattedTags.value = [...formattedTags.value, trimmedTag];
    newTag.value = '';
  } else if (trimmedTag) {
    ElMessage.warning('标签已存在');
  }
};

// 移除标签
const removeTag = (index) => {
  formattedTags.value = formattedTags.value.filter((_, i) => i !== index);
};

// 更新用户信息
const updateUser = async () => {
  if (user.value) {
    user.value.tags = JSON.stringify(formattedTags.value);
  }

  try {
    const response = await updateUserInfo(userId, user.value);
    if (response?.data?.code === 200) {
      ElMessage.success('用户信息更新成功');
      isEditing.value = false;
    } else {
      ElMessage.error('更新失败，请检查输入');
    }
  } catch (error) {
    ElMessage.error('更新失败: ' + error.message);
  }
};
</script>

<style scoped>
.username-container {
  display: flex;
  align-items: center;
  margin-left: 10px;
}
.username-input {
  max-width: 200px;
}
.avatar-section {
  display: flex;
  flex-direction: row;
  align-items: center;
  height: 120px;
  margin: -20px;
  border: 1px solid #E8E8E8;
}
.el-avatar {
  margin-left: 10px;
  margin-right: 30px;
}
.info-section {
  padding: 10px;
  margin: 60px -20px;
  border: 1px solid #E8E8E8;
}
.info-row {
  margin: 10px 0;
}
.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 5px; /* 间距 */
}
</style>
