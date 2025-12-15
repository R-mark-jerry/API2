<template>
  <div class="sidebar-container">
    <div class="sidebar-logo">
      <h1 class="logo-title">API管理平台</h1>
    </div>
    
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="true"
        :collapse-transition="false"
        mode="vertical"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <sidebar-item
          v-for="route in routes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import SidebarItem from './SidebarItem.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = computed(() => false) // 可以从store获取侧边栏折叠状态

const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

// 过滤出需要显示在菜单中的路由
const routes = computed(() => {
  const filterRoutes = (routes) => {
    return routes.filter(route => {
      // 过滤掉不需要显示的路由
      if (route.meta && route.meta.hidden) {
        return false
      }
      
      // 如果有子路由，递归过滤
      if (route.children && route.children.length > 0) {
        route.children = filterRoutes(route.children)
      }
      
      return true
    })
  }
  
  // 从路由配置中获取需要显示的路由
  const allRoutes = router.options.routes.filter(route => {
    // 过滤掉登录页面和404页面
    if (route.path === '/login' || route.path === '/:pathMatch(.*)*' || route.path === '/404') {
      return false
    }
    return true
  })
  
  return filterRoutes(allRoutes)
})
</script>

<style lang="scss" scoped>
.sidebar-container {
  transition: width 0.28s;
  width: 210px;
  background-color: #304156;
  height: 100%;
  position: fixed;
  font-size: 0px;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;

  // reset element-ui css
  .horizontal-collapse-transition {
    transition: 0s width ease-in-out, 0s padding-left ease-in-out, 0s padding-right ease-in-out;
  }

  .scrollbar-wrapper {
    overflow-x: hidden !important;
  }

  .el-scrollbar__bar.is-vertical {
    right: 0px;
  }

  .el-scrollbar {
    height: 100%;
  }

  &.has-logo {
    .el-scrollbar {
      height: calc(100% - 50px);
    }
  }

  .is-horizontal {
    display: none;
  }

  a {
    display: inline-block;
    width: 100%;
    overflow: hidden;
  }

  .svg-icon {
    margin-right: 16px;
  }

  .sub-el-icon {
    margin-right: 12px;
    margin-left: -2px;
  }

  .el-menu {
    border: none;
    height: 100%;
    width: 100% !important;
  }

  // menu hover
  .submenu-title-noDropdown,
  .el-submenu__title {
    &:hover {
      background-color: #263445 !important;
    }
  }

  .is-active > .el-submenu__title {
    color: #f4f4f5 !important;
  }

  & .nest-menu .el-submenu > .el-submenu__title,
  & .el-submenu .el-menu-item {
    min-width: 210px !important;
    background-color: #1f2d3d !important;

    &:hover {
      background-color: #001528 !important;
    }
  }
}

.sidebar-logo {
  height: 50px;
  line-height: 50px;
  background: #2b2f3a;
  text-align: center;
  
  .logo-title {
    display: inline-block;
    margin: 0;
    color: #fff;
    font-weight: 600;
    line-height: 50px;
    font-size: 14px;
    font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
    vertical-align: middle;
  }
}
</style>