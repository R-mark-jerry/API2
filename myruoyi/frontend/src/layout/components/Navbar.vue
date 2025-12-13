<template>
  <div class="navbar">
    <div class="navbar-left">
      <hamburger
        :is-active="!sidebarCollapsed"
        class="hamburger-container"
        @toggle-click="toggleSideBar"
      />
      
      <breadcrumb class="breadcrumb-container" />
    </div>
    
    <div class="navbar-right">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <el-avatar :size="30" :src="userStore.avatar || defaultAvatar" />
          <span class="user-name">{{ userStore.nickName || userStore.userName }}</span>
          <el-icon class="el-icon--right">
            <arrow-down />
          </el-icon>
        </div>
        
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="handleProfile">
              <el-icon><User /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item divided @click="handleLogout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import Hamburger from '@/components/Hamburger/index.vue'
import Breadcrumb from '@/components/Breadcrumb/index.vue'

const router = useRouter()
const userStore = useUserStore()

const sidebarCollapsed = computed(() => false) // 这里可以从store获取侧边栏状态

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const toggleSideBar = () => {
  // 切换侧边栏展开/收起状态
  // 这里可以调用store中的方法
}

const handleProfile = () => {
  // 跳转到个人中心页面
  router.push('/profile')
}

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await userStore.logout()
    ElMessage.success('退出登录成功')
    router.push('/login')
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  
  .navbar-left {
    display: flex;
    align-items: center;
    
    .hamburger-container {
      cursor: pointer;
      transition: background 0.3s;
      
      &:hover {
        background: rgba(0, 0, 0, 0.025);
      }
    }
    
    .breadcrumb-container {
      margin-left: 20px;
    }
  }
  
  .navbar-right {
    display: flex;
    align-items: center;
    
    .avatar-container {
      cursor: pointer;
      
      .avatar-wrapper {
        display: flex;
        align-items: center;
        
        .user-name {
          margin-left: 8px;
          margin-right: 8px;
          font-size: 14px;
          color: #606266;
        }
      }
    }
  }
}
</style>