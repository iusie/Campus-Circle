import { createRouter, createWebHistory } from 'vue-router'
import BaseLayout from '@/components/BaseLayout.vue'
import UserInfo from '@/views/user/UserInfo.vue'
import PersonInfo from '@/views/user/PersonInfo.vue'
import SecurityCenter from '@/views/user/SecurityCenter.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: BaseLayout,
    },
    {
      path: '/userInfo',
      component: UserInfo,
      children: [
        {
          path: 'personInfo',
          component: PersonInfo,
        },
        {
          path: 'securityCenter',
          component: SecurityCenter,
        },
      ],
    },
  ],
})

export default router
