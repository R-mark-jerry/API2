<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>应用总数</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.appCount || 0 }}</div>
            <div class="card-desc">API应用数量</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>模块总数</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.moduleCount || 0 }}</div>
            <div class="card-desc">API模块数量</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>接口总数</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.apiCount || 0 }}</div>
            <div class="card-desc">API接口数量</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>今日调用</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.todayCallCount || 0 }}</div>
            <div class="card-desc">今日API调用次数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="mt-20">
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>最近创建的应用</span>
            </div>
          </template>
          <el-table :data="recentApps" style="width: 100%">
            <el-table-column prop="appName" label="应用名称" />
            <el-table-column prop="appCode" label="应用编码" />
            <el-table-column prop="createTime" label="创建时间" width="180" />
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>最近创建的接口</span>
            </div>
          </template>
          <el-table :data="recentApis" style="width: 100%">
            <el-table-column prop="apiName" label="接口名称" />
            <el-table-column prop="apiCode" label="接口编码" />
            <el-table-column prop="createTime" label="创建时间" width="180" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listApp } from '@/api/app'
import { listInfo } from '@/api/info'

const statistics = ref({
  appCount: 0,
  moduleCount: 0,
  apiCount: 0,
  todayCallCount: 0
})

const recentApps = ref([])
const recentApis = ref([])

const getStatistics = async () => {
  try {
    // 获取应用总数
    const appRes = await listApp({})
    statistics.value.appCount = appRes.data.length || 0
    
    // 获取接口总数
    const apiRes = await listInfo({})
    statistics.value.apiCount = apiRes.data.length || 0
    
    // 模拟数据，实际应该从后端获取
    statistics.value.moduleCount = 12
    statistics.value.todayCallCount = 328
    
    // 获取最近创建的应用
    recentApps.value = appRes.data.slice(0, 5).map(item => ({
      ...item,
      createTime: item.createTime ? new Date(item.createTime).toLocaleString() : '-'
    }))
    
    // 获取最近创建的接口
    recentApis.value = apiRes.data.slice(0, 5).map(item => ({
      ...item,
      createTime: item.createTime ? new Date(item.createTime).toLocaleString() : '-'
    }))
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

onMounted(() => {
  getStatistics()
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  
  .box-card {
    margin-bottom: 20px;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .card-content {
      text-align: center;
      
      .card-value {
        font-size: 32px;
        font-weight: bold;
        color: #409eff;
        margin-bottom: 10px;
      }
      
      .card-desc {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}
</style>