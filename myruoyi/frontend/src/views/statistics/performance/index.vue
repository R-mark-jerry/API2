<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
      <el-form-item label="应用名称" prop="appName">
        <el-select v-model="queryParams.appId" placeholder="请选择应用" clearable>
          <el-option
            v-for="app in appOptions"
            :key="app.appId"
            :label="app.appName"
            :value="app.appId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="统计周期" prop="period">
        <el-select v-model="queryParams.period" placeholder="请选择统计周期">
          <el-option
            v-for="dict in periodOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围" prop="dateRange">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button type="warning" icon="Download" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>

    <!-- 性能指标卡片 -->
    <el-row :gutter="20" class="mb8">
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>平均响应时间</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.avgResponseTime || 0 }}ms</div>
            <div class="card-desc">API平均响应时间</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>最大响应时间</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.maxResponseTime || 0 }}ms</div>
            <div class="card-desc">API最大响应时间</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>最小响应时间</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.minResponseTime || 0 }}ms</div>
            <div class="card-desc">API最小响应时间</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>QPS</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.qps || 0 }}</div>
            <div class="card-desc">每秒请求数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="mb8">
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>响应时间趋势</span>
            </div>
          </template>
          <div id="responseTimeChart" style="height: 350px;"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>QPS趋势</span>
            </div>
          </template>
          <div id="qpsChart" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 慢接口分析 -->
    <el-card class="box-card mb8">
      <template #header>
        <div class="card-header">
          <span>慢接口分析</span>
        </div>
      </template>
      <el-table v-loading="loading" :data="slowApiList">
        <el-table-column label="应用名称" align="center" prop="appName" />
        <el-table-column label="接口名称" align="center" prop="apiName" :show-overflow-tooltip="true" />
        <el-table-column label="平均响应时间" align="center" prop="avgResponseTime">
          <template #default="scope">
            <el-tag v-if="scope.row.avgResponseTime > 1000" type="danger">{{ scope.row.avgResponseTime }}ms</el-tag>
            <el-tag v-else-if="scope.row.avgResponseTime > 500" type="warning">{{ scope.row.avgResponseTime }}ms</el-tag>
            <el-tag v-else type="success">{{ scope.row.avgResponseTime }}ms</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最大响应时间" align="center" prop="maxResponseTime" />
        <el-table-column label="调用次数" align="center" prop="callCount" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="text"
              icon="View"
              @click="handleDetail(scope.row)"
            >详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 接口性能表格 -->
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>接口性能分析</span>
        </div>
      </template>
      <el-table v-loading="loading" :data="performanceList">
        <el-table-column label="应用名称" align="center" prop="appName" />
        <el-table-column label="接口名称" align="center" prop="apiName" :show-overflow-tooltip="true" />
        <el-table-column label="平均响应时间" align="center" prop="avgResponseTime" />
        <el-table-column label="最大响应时间" align="center" prop="maxResponseTime" />
        <el-table-column label="最小响应时间" align="center" prop="minResponseTime" />
        <el-table-column label="调用次数" align="center" prop="callCount" />
        <el-table-column label="成功率" align="center" prop="successRate">
          <template #default="scope">
            <el-progress 
              :percentage="scope.row.successRate" 
              :color="getProgressColor(scope.row.successRate)"
            ></el-progress>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="text"
              icon="View"
              @click="handleDetail(scope.row)"
            >详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 性能详情对话框 -->
    <el-dialog title="性能详情" v-model="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="应用名称">{{ performanceDetail.appName }}</el-descriptions-item>
        <el-descriptions-item label="接口名称">{{ performanceDetail.apiName }}</el-descriptions-item>
        <el-descriptions-item label="平均响应时间">{{ performanceDetail.avgResponseTime }}ms</el-descriptions-item>
        <el-descriptions-item label="最大响应时间">{{ performanceDetail.maxResponseTime }}ms</el-descriptions-item>
        <el-descriptions-item label="最小响应时间">{{ performanceDetail.minResponseTime }}ms</el-descriptions-item>
        <el-descriptions-item label="调用次数">{{ performanceDetail.callCount }}</el-descriptions-item>
        <el-descriptions-item label="成功率">{{ performanceDetail.successRate }}%</el-descriptions-item>
        <el-descriptions-item label="QPS">{{ performanceDetail.qps }}</el-descriptions-item>
      </el-descriptions>
      
      <el-divider>性能趋势图</el-divider>
      <div id="detailChart" style="height: 300px;"></div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailOpen = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, toRefs, getCurrentInstance, nextTick } from 'vue'
import { parseTime } from '@/utils/ruoyi'
import * as echarts from 'echarts'
import { listApp } from '@/api/app'

const { proxy } = getCurrentInstance()

const performanceList = ref([])
const slowApiList = ref([])
const appOptions = ref([])
const detailOpen = ref(false)
const loading = ref(true)
const total = ref(0)
const dateRange = ref([])
const performanceDetail = ref({})
const responseTimeChart = ref(null)
const qpsChart = ref(null)
const detailChart = ref(null)

const periodOptions = ref([
  { label: '按小时', value: 'hour' },
  { label: '按天', value: 'day' },
  { label: '按周', value: 'week' },
  { label: '按月', value: 'month' }
])

const statistics = ref({
  avgResponseTime: 0,
  maxResponseTime: 0,
  minResponseTime: 0,
  qps: 0
})

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    appId: undefined,
    period: 'day'
  }
})

const { queryParams } = toRefs(data)

/** 查询性能分析列表 */
function getList() {
  loading.value = true
  
  // 模拟数据
  setTimeout(() => {
    performanceList.value = [
      {
        id: 1,
        appName: '测试应用',
        apiName: '获取用户信息',
        avgResponseTime: 120,
        maxResponseTime: 350,
        minResponseTime: 50,
        callCount: 1250,
        successRate: 98.5,
        qps: 15.2
      },
      {
        id: 2,
        appName: '测试应用',
        apiName: '更新用户信息',
        avgResponseTime: 250,
        maxResponseTime: 800,
        minResponseTime: 100,
        callCount: 320,
        successRate: 95.2,
        qps: 4.1
      },
      {
        id: 3,
        appName: '测试应用',
        apiName: '批量导入数据',
        avgResponseTime: 1200,
        maxResponseTime: 2500,
        minResponseTime: 800,
        callCount: 45,
        successRate: 88.9,
        qps: 0.8
      }
    ]
    
    slowApiList.value = performanceList.value.filter(item => item.avgResponseTime > 500)
    
    total.value = performanceList.value.length
    
    // 更新统计数据
    statistics.value.avgResponseTime = 280
    statistics.value.maxResponseTime = 2500
    statistics.value.minResponseTime = 50
    statistics.value.qps = 12.8
    
    loading.value = false
    
    // 绘制图表
    nextTick(() => {
      drawResponseTimeChart()
      drawQpsChart()
    })
  }, 500)
}

/** 查询应用列表 */
function getAppList() {
  listApp({}).then(response => {
    appOptions.value = response.data || []
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('statistics/performance/export', {
    ...queryParams.value,
    startTime: dateRange.value[0],
    endTime: dateRange.value[1]
  }, `性能分析_${new Date().getTime()}.xlsx`)
}

/** 详情按钮操作 */
function handleDetail(row) {
  performanceDetail.value = { ...row }
  detailOpen.value = true
  
  nextTick(() => {
    drawDetailChart(row)
  })
}

/** 绘制响应时间趋势图 */
function drawResponseTimeChart() {
  const chartDom = document.getElementById('responseTimeChart')
  if (!chartDom) return
  
  if (responseTimeChart.value) {
    responseTimeChart.value.dispose()
  }
  
  responseTimeChart.value = echarts.init(chartDom)
  
  const option = {
    title: {
      text: '响应时间趋势'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['平均响应时间', '最大响应时间']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00']
    },
    yAxis: {
      type: 'value',
      name: '响应时间(ms)'
    },
    series: [
      {
        name: '平均响应时间',
        type: 'line',
        data: [120, 132, 101, 134, 90, 230, 210]
      },
      {
        name: '最大响应时间',
        type: 'line',
        data: [220, 282, 191, 234, 190, 330, 310]
      }
    ]
  }
  
  responseTimeChart.value.setOption(option)
}

/** 绘制QPS趋势图 */
function drawQpsChart() {
  const chartDom = document.getElementById('qpsChart')
  if (!chartDom) return
  
  if (qpsChart.value) {
    qpsChart.value.dispose()
  }
  
  qpsChart.value = echarts.init(chartDom)
  
  const option = {
    title: {
      text: 'QPS趋势'
    },
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00']
    },
    yAxis: {
      type: 'value',
      name: 'QPS'
    },
    series: [
      {
        name: 'QPS',
        type: 'line',
        areaStyle: {},
        data: [5, 8, 15, 25, 18, 12, 8]
      }
    ]
  }
  
  qpsChart.value.setOption(option)
}

/** 绘制详情图表 */
function drawDetailChart(row) {
  const chartDom = document.getElementById('detailChart')
  if (!chartDom) return
  
  if (detailChart.value) {
    detailChart.value.dispose()
  }
  
  detailChart.value = echarts.init(chartDom)
  
  const option = {
    title: {
      text: `${row.apiName} 性能趋势`
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['响应时间', 'QPS']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00']
    },
    yAxis: [
      {
        type: 'value',
        name: '响应时间(ms)',
        position: 'left'
      },
      {
        type: 'value',
        name: 'QPS',
        position: 'right'
      }
    ],
    series: [
      {
        name: '响应时间',
        type: 'line',
        yAxisIndex: 0,
        data: [120, 132, 101, 134, 90, 230, 210]
      },
      {
        name: 'QPS',
        type: 'line',
        yAxisIndex: 1,
        data: [5, 8, 15, 25, 18, 12, 8]
      }
    ]
  }
  
  detailChart.value.setOption(option)
}

/** 获取进度条颜色 */
function getProgressColor(percentage) {
  if (percentage >= 95) return '#67c23a'
  if (percentage >= 85) return '#e6a23c'
  return '#f56c6c'
}

onMounted(() => {
  getList()
  getAppList()
  
  // 监听窗口大小变化，重新绘制图表
  window.addEventListener('resize', () => {
    if (responseTimeChart.value) {
      responseTimeChart.value.resize()
    }
    if (qpsChart.value) {
      qpsChart.value.resize()
    }
    if (detailChart.value) {
      detailChart.value.resize()
    }
  })
})
</script>

<style lang="scss" scoped>
.app-container {
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