import { createRouter, createWebHistory } from 'vue-router'
import BaseLayout from '@/components/BaseLayout.vue'
import UserInfo from '@/views/user/UserInfo.vue'
import PersonInfo from '@/views/user/PersonInfo.vue'
import SecurityCenter from '@/views/user/SecurityCenter.vue'
import MainBodyView from '@/components/MainBodyView.vue'
import BaseInfo from '@/views/base/IndexInfo.vue'
import WriteArticles from '@/views/article/WriteArticles.vue'
import Articles from '@/views/article/ArticleList.vue'
import ArticleComponent from '@/views/article/ArticleComponent.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: BaseLayout,
      children: [
        {
          path: '',
          redirect: 'home', // 默认重定向到 /main
        },
        {
          path: 'home',
          component: MainBodyView, // 默认显示的组件
        },
        {
          path: 'articles',
          component: Articles,
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
        {
          path: '/BaseInfoView',
          component: BaseInfo,
        },

      ],


    },
    {
      path: '/write',
      component: WriteArticles,
    },
    {
      path: '/test',
      component: ArticleComponent,
    },
  ],

})

export default router
