<template>
  <div v-if="!item.meta || !item.meta.hidden">
    <template v-if="hasOneShowingChild(item.children, item)">
      <el-menu-item
        v-if="onlyOneChild.meta"
        :index="resolvePath(onlyOneChild.path)"
      >
        <el-icon v-if="onlyOneChild.meta.icon">
          <component :is="onlyOneChild.meta.icon" />
        </el-icon>
        <template #title>{{ onlyOneChild.meta.title }}</template>
      </el-menu-item>
    </template>
    
    <el-sub-menu
      v-else
      ref="subMenu"
      :index="resolvePath(item.path)"
      popper-append-to-body
    >
      <template #title>
        <el-icon v-if="item.meta && item.meta.icon">
          <component :is="item.meta.icon" />
        </el-icon>
        <span>{{ item.meta && item.meta.title }}</span>
      </template>
      
      <template v-for="child in item.children" :key="child.path">
        <el-menu-item
          v-if="!child.meta || !child.meta.hidden"
          :index="resolvePath(child.path)"
          @click="handleChildMenuClick(child)"
        >
          <el-icon v-if="child.meta && child.meta.icon">
            <component :is="child.meta.icon" />
          </el-icon>
          <template #title>{{ child.meta && child.meta.title }}</template>
        </el-menu-item>
      </template>
    </el-sub-menu>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { isExternal } from '@/utils/validate'
import { getNormalPath } from '@/utils/ruoyi'

const props = defineProps({
  // route object
  item: {
    type: Object,
    required: true
  },
  isNest: {
    type: Boolean,
    default: false
  },
  basePath: {
    type: String,
    default: ''
  }
})

const router = useRouter()
const subMenu = ref(null)
const onlyOneChild = ref({})

const hasOneShowingChild = (children = [], parent) => {
  const showingChildren = children.filter(item => {
    if (item.meta && item.meta.hidden) {
      return false
    } else {
      // 临时设置
      onlyOneChild.value = item
      return true
    }
  })

  // 当只有一个子路由时，默认显示该子路由
  if (showingChildren.length === 1) {
    return true
  }

  // 没有子路由则显示父路由
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }

  return false
}

const resolvePath = (routePath, routeQuery) => {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  
  // 如果routePath已经是绝对路径（以/开头），则直接使用
  if (routePath.startsWith('/')) {
    if (routeQuery) {
      let query = JSON.parse(JSON.stringify(routeQuery))
      return getNormalPath(routePath, query)
    }
    return getNormalPath(routePath)
  }
  
  // 否则拼接basePath和routePath
  if (routeQuery) {
    let query = JSON.parse(JSON.stringify(routeQuery))
    return getNormalPath(props.basePath + '/' + routePath, query)
  }
  return getNormalPath(props.basePath + '/' + routePath)
}

const handleMenuClick = (path) => {
  if (path) {
    router.push(path)
  }
}

const handleChildMenuClick = (child) => {
  const childPath = resolvePath(child.path)
  if (childPath) {
    router.push(childPath)
  }
}
</script>

<style lang="scss" scoped>
.svg-icon {
  margin-right: 16px;
}

.sub-el-icon {
  margin-right: 12px;
  margin-left: -2px;
}

// 确保菜单项可以点击
.el-menu-item {
  pointer-events: auto !important;
  cursor: pointer !important;
  position: relative;
  z-index: 10;
}

.el-sub-menu {
  pointer-events: auto !important;
  
  .el-sub-menu__title {
    cursor: pointer !important;
    pointer-events: auto !important;
  }
}

// 确保菜单容器不会阻止点击事件
div {
  pointer-events: auto;
}
</style>