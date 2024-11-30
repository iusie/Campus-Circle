import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userId: localStorage.getItem('userId') ? BigInt(localStorage.getItem('userId')!) : null,
    isAdmin: localStorage.getItem('isAdmin') === '1', // 读取 '1' 为 true，其他为 false
  }),
  actions: {
    setUser(id: bigint, adminStatus: boolean) {
      this.userId = id;
      this.isAdmin = adminStatus;
      localStorage.setItem('userId', id.toString()); // 存储 userId
      localStorage.setItem('isAdmin', adminStatus ? '1' : '0'); // 存储 isAdmin 作为 '1' 或 '0'
    },
    clearUser() {
      this.userId = null;
      this.isAdmin = false;
      localStorage.removeItem('userId');
      localStorage.removeItem('isAdmin');
    },
  },
});
