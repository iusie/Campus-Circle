import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userId: localStorage.getItem('userId') ? BigInt(localStorage.getItem('userId')!) : null,
    isAdmin: localStorage.getItem('isAdmin') === '1', // 读取 '1' 为 true，其他为 false
    CacheToken: localStorage.getItem('CacheToken') || '',
  }),
  actions: {
    setUser(id: bigint, adminStatus: boolean , token: string) {
      this.userId = id;
      this.isAdmin = adminStatus;
      this.CacheToken = token;
      localStorage.setItem('userId', id.toString()); // 存储 userId
      localStorage.setItem('isAdmin', adminStatus ? '1' : '0'); // 存储 isAdmin 作为 '1' 或 '0'
      localStorage.setItem('CacheToken', token);
    },
    clearUser() {
      this.userId = null;
      this.isAdmin = false;
      this.CacheToken = '';
      localStorage.removeItem('userId');
      localStorage.removeItem('isAdmin');
      localStorage.removeItem('CacheToken');
    },
  },
});
