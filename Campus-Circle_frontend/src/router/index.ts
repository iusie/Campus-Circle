import { createRouter, createWebHistory } from 'vue-router'
import BaseLayout from '@/components/BaseLayout.vue'
import LoginRegisterView from '@/views/login/LoginRegisterView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: BaseLayout,
    },

    {
      path: '/test',
      name: 'test',
      component: LoginRegisterView,
    },
  ],
})

export default router
