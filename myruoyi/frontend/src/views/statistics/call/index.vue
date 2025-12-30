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

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb8">
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>总调用次数</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.totalCalls || 0 }}</div>
            <div class="card-desc">API总调用次数</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>成功调用次数</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.successCalls || 0 }}</div>
            <div class="card-desc">成功调用次数</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>失败调用次数</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.failCalls || 0 }}</div>
            <div class="card-desc">失败调用次数</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>成功率</span>
            </div>
          </template>
          <div class="card-content">
            <div class="card-value">{{ statistics.successRate || 0 }}%</div>
            <div class="card-desc">API调用成功率</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="mb8">
      <el-col :span="24">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>调用趋势图</span>
            </div>
          </template>
          <div id="callChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 调用记录表格 -->
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>调用记录</span>
        </div>
      </template>
      <el-table v-loading="loading" :data="callList">
        <el-table-column label="应用名称" align="center" prop="appName" />
        <el-table-column label="接口名称" align="center" prop="apiName" :show-overflow-tooltip="true" />
        <el-table-column label="调用时间" align="center" prop="callTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.callTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="调用状态" align="center" prop="callStatus">
          <template #default="scope">
            <dict-tag :options="statusOptions" :value="scope.row.callStatus"/>
          </template>
        </el-table-column>
        <el-table-column label="响应时间(ms)" align="center" prop="responseTime" />
        <el-table-column label="调用IP" align="center" prop="callerIp" :show-overflow-tooltip="true" />
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

    <!-- 调用详情对话框 -->
    <el-dialog title="调用详情" v-model="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="应用名称">{{ callDetail.appName }}</el-descriptions-item>
        <el-descriptions-item label="接口名称">{{ callDetail.apiName }}</el-descriptions-item>
        <el-descriptions-item label="调用时间">{{ parseTime(callDetail.callTime) }}</el-descriptions-item>
        <el-descriptions-item label="调用状态">
          <dict-tag :options="statusOptions" :value="callDetail.callStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="响应时间">{{ callDetail.responseTime }}ms</el-descriptions-item>
        <el-descriptions-item label="调用IP">{{ callDetail.callerIp }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <pre>{{ callDetail.requestParams }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="响应结果" :span="2">
          <pre>{{ callDetail.responseResult }}</pre>
        </el-descriptions-item>
      </el-descriptions>
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

const callList = ref([])
const appOptions = ref([])
const detailOpen = ref(false)
const loading = ref(true)
const total = ref(0)
const dateRange = ref([])
const callDetail = ref({})
const callChart = ref(null)

const periodOptions = ref([
  { label: '按小时', value: 'hour' },
  { label: '按天', value: 'day' },
  { label: '按周', value: 'week' },
  { label: '按月', value: 'month' }
])

const statusOptions = ref([
  { label: '成功', value: '0' },
  { label: '失败', value: '1' }
])

const statistics = ref({
  totalCalls: 0,
  successCalls: 0,
  failCalls: 0,
  successRate: 0
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

/** 查询调用记录列表 */
function getList() {
  loading.value = true
  
  // 模拟数据
  setTimeout(() => {
    callList.value = [
      {
        id: 1,
        appName: '测试应用',
        apiName: '获取用户信息',
        callTime: new Date(),
        callStatus: '0',
        responseTime: 120,
        callerIp: '192.168.1.100',
        requestParams: '{ userId: 1 }',
        responseResult: '{ code: 200, data: {...} }'
      },
      {
        id: 2,
        appName: '测试应用',
        apiName: '更新用户信息',
        callTime: new Date(Date.now() - 3600000),
        callStatus: '1',
        responseTime: 250,
        callerIp: '192.168.1.101',
        requestParams: '{ userId: 1, name: "test" }',
        responseResult: '{ code: 500, msg: "服务器错误" }'
      }
    ]
    total.value = 2
    
    // 更新统计数据
    statistics.value.totalCalls = 1250
    statistics.value.successCalls = 1180
    statistics.value.failCalls = 70
    statistics.value.successRate = 94.4
    
    loading.value = false
    
    // 绘制图表
    nextTick(() => {
      drawChart()
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
  proxy.download('statistics/call/export', {
    ...queryParams.value,
    startTime: dateRange.value[0],
    endTime: dateRange.value[1]
  }, `调用统计_${new Date().getTime()}.xlsx`)
}

/** 详情按钮操作 */
function handleDetail(row) {
  callDetail.value = { ...row }
  detailOpen.value = true
}

/** 绘制图表 */
function drawChart() {
  const chartDom = document.getElementById('callChart')
  if (!chartDom) return
  
  if (callChart.value) {
    callChart.value.dispose()
  }
  
  callChart.value = echarts.init(chartDom)
  
  const option = {
    title: {
      text: 'API调用趋势'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['成功调用', '失败调用']
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
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '成功调用',
        type: 'line',
        stack: 'Total',
        data: [120, 132, 101, 134, 90, 230, 210]
      },
      {
        name: '失败调用',
        type: 'line',
        stack: 'Total',
        data: [5, 8, 3, 6, 2, 10, 7]
      }
    ]
  }
  
  callChart.value.setOption(option)
}

onMounted(() => {
  getList()
  getAppList()
  
  // 监听窗口大小变化，重新绘制图表
  window.addEventListener('resize', () => {
    if (callChart.value) {
      callChart.value.resize()
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