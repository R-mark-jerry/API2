import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    meta: { title: '首页', requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '工作台', icon: 'House', requiresAuth: true }
      }
    ]
  },
  {
    path: '/api',
    component: () => import('@/layout/index.vue'),
    redirect: '/api/app',
    meta: { title: 'API管理', icon: 'Document', requiresAuth: true },
    children: [
      {
        path: 'app',
        name: 'ApiApp',
        component: () => import('@/views/api/app/index.vue'),
        meta: { title: '应用管理', icon: 'Collection', requiresAuth: true }
      },
      {
        path: 'module',
        name: 'ApiModule',
        component: () => import('@/views/api/module/index.vue'),
        meta: { title: '模块管理', icon: 'FolderOpened', requiresAuth: true }
      },
      {
        path: 'info',
        name: 'ApiInfo',
        component: () => import('@/views/api/info/index.vue'),
        meta: { title: '接口管理', icon: 'List', requiresAuth: true }
      },
      {
        path: 'permission',
        name: 'ApiPermission',
        component: () => import('@/views/api/permission/index.vue'),
        meta: { title: '权限管理', icon: 'Lock', requiresAuth: true }
      }
    ]
  },
  {
    path: '/statistics',
    component: () => import('@/layout/index.vue'),
    redirect: '/statistics/call',
    meta: { title: '统计分析', icon: 'DataAnalysis', requiresAuth: true },
    children: [
      {
        path: 'call',
        name: 'StatisticsCall',
        component: () => import('@/views/statistics/call/index.vue'),
        meta: { title: '调用统计', icon: 'TrendCharts', requiresAuth: true }
      },
      {
        path: 'performance',
        name: 'StatisticsPerformance',
        component: () => import('@/views/statistics/performance/index.vue'),
        meta: { title: '性能分析', icon: 'Timer', requiresAuth: true }
      }
    ]
  },
  {
    path: '/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'Setting', requiresAuth: true },
    children: [
      {
        path: 'user',
        name: 'SystemUser',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'User', requiresAuth: true }
      }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '404', requiresAuth: false }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    meta: { requiresAuth: false }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  NProgress.start()
  
  const userStore = useUserStore()
  const hasToken = userStore.token
  
  if (to.meta.requiresAuth) {
    if (hasToken) {
      if (to.path === '/login') {
        next({ path: '/' })
      } else {
        // 检查是否已获取用户信息
        if (!userStore.userId) {
          try {
            await userStore.getUserInfo()
            next()
          } catch (error) {
            // 获取用户信息失败，可能是token过期
            await userStore.logout()
            next(`/login?redirect=${to.path}`)
          }
        } else {
          next()
        }
      }
    } else {
      // 没有token，跳转到登录页
      next(`/login?redirect=${to.path}`)
    }
  } else {
    // 不需要认证的页面
    if (to.path === '/login' && hasToken) {
      next({ path: '/' })
    } else {
      next()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router