<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <sidebar class="sidebar-container" />
    
    <div class="main-container">
      <!-- 顶部导航 -->
      <navbar class="navbar" />
      
      <!-- 主要内容区域 -->
      <app-main />
    </div>
  </div>
</template>

<script setup>
import Navbar from './components/Navbar.vue'
import Sidebar from './components/Sidebar.vue'
import AppMain from './components/AppMain.vue'
</script>

<style lang="scss" scoped>
.app-wrapper {
  display: flex;
  width: 100%;
  height: 100vh;
  
  .sidebar-container {
    width: 210px;
    height: 100vh;
    background-color: #304156;
    transition: width 0.28s;
    overflow: hidden;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 1001;
  }
  
  .main-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    margin-left: 210px; // 为侧边栏留出空间
    min-height: 100vh;
    
    .navbar {
      height: 50px;
      background-color: #fff;
      box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
      position: relative;
      z-index: 1000;
    }
  }
}
</style>