<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用ID" prop="appId">
        <el-select v-model="queryParams.appId" placeholder="请选择应用" clearable>
          <el-option
            v-for="app in appOptions"
            :key="app.appId"
            :label="app.appName"
            :value="app.appId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="模块ID" prop="moduleId">
        <el-select v-model="queryParams.moduleId" placeholder="请选择模块" clearable>
          <el-option
            v-for="module in moduleOptions"
            :key="module.moduleId"
            :label="module.moduleName"
            :value="module.moduleId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="API编码" prop="apiCode">
        <el-input
          v-model="queryParams.apiCode"
          placeholder="请输入API编码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="API名称" prop="apiName">
        <el-input
          v-model="queryParams.apiName"
          placeholder="请输入API名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请求URL" prop="requestUrl">
        <el-input
          v-model="queryParams.requestUrl"
          placeholder="请输入请求URL"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="API状态" prop="apiStatus">
        <el-select v-model="queryParams.apiStatus" placeholder="API状态" clearable>
          <el-option
            v-for="dict in apiStatusOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="发布状态" prop="publishStatus">
        <el-select v-model="queryParams.publishStatus" placeholder="发布状态" clearable>
          <el-option
            v-for="dict in publishStatusOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="batchDelete"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="API编号" align="center" prop="apiId" />
      <el-table-column label="应用名称" align="center" prop="appName" :show-overflow-tooltip="true" />
      <el-table-column label="模块名称" align="center" prop="moduleName" :show-overflow-tooltip="true" />
      <el-table-column label="API编码" align="center" prop="apiCode" :show-overflow-tooltip="true" />
      <el-table-column label="API名称" align="center" prop="apiName" :show-overflow-tooltip="true" />
      <el-table-column label="请求方法" align="center" prop="requestMethod" width="100">
        <template #default="scope">
          <el-tag :type="getMethodType(scope.row.requestMethod)" size="small">
            {{ scope.row.requestMethod }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="API状态" align="center" prop="apiStatus" width="100">
        <template #default="scope">
          <dict-tag :options="apiStatusOptions" :value="scope.row.apiStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" align="center" prop="publishStatus" width="100">
        <template #default="scope">
          <dict-tag :options="publishStatusOptions" :value="scope.row.publishStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            type="text"
            icon="View"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            type="text"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['api:info:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="Delete"
            @click="directDelete(scope.row)"
            v-hasPermi="['api:info:remove']"
          >删除</el-button>
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

    <!-- 添加或修改API接口对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body ref="infoDialog">
      <el-form ref="infoRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属应用" prop="appId">
              <el-select v-model="form.appId" placeholder="请选择应用">
                <el-option
                  v-for="app in appOptions"
                  :key="app.appId"
                  :label="app.appName"
                  :value="app.appId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属模块" prop="moduleId">
              <el-select v-model="form.moduleId" placeholder="请选择模块">
                <el-option
                  v-for="module in moduleOptions"
                  :key="module.moduleId"
                  :label="module.moduleName"
                  :value="module.moduleId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="API编码" prop="apiCode">
              <el-input v-model="form.apiCode" placeholder="请输入API编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API名称" prop="apiName">
              <el-input v-model="form.apiName" placeholder="请输入API名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="请求方法" prop="requestMethod">
              <el-select v-model="form.requestMethod" placeholder="请选择请求方法">
                <el-option label="GET" value="GET" />
                <el-option label="POST" value="POST" />
                <el-option label="PUT" value="PUT" />
                <el-option label="DELETE" value="DELETE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内容类型" prop="contentType">
              <el-input v-model="form.contentType" placeholder="请输入内容类型" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="请求URL" prop="requestUrl">
          <el-input v-model="form.requestUrl" placeholder="请输入请求URL" />
        </el-form-item>
        <el-form-item label="API描述" prop="apiDesc">
          <el-input v-model="form.apiDesc" type="textarea" placeholder="请输入API描述" />
        </el-form-item>
        <el-form-item label="请求参数" prop="requestParams">
          <el-input v-model="form.requestParams" type="textarea" :rows="4" placeholder="请输入请求参数" />
        </el-form-item>
        <el-form-item label="响应参数" prop="responseParams">
          <el-input v-model="form.responseParams" type="textarea" :rows="4" placeholder="请输入响应参数" />
        </el-form-item>
        <el-form-item label="请求示例" prop="requestExample">
          <el-input v-model="form.requestExample" type="textarea" :rows="4" placeholder="请输入请求示例" />
        </el-form-item>
        <el-form-item label="响应示例" prop="responseExample">
          <el-input v-model="form.responseExample" type="textarea" :rows="4" placeholder="请输入响应示例" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="API状态" prop="apiStatus">
              <el-radio-group v-model="form.apiStatus">
                <el-radio
                  v-for="dict in apiStatusOptions"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发布状态" prop="publishStatus">
              <el-radio-group v-model="form.publishStatus">
                <el-radio
                  v-for="dict in publishStatusOptions"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="负责人" prop="responsibleUserId">
              <el-select
                v-model="form.responsibleUserId"
                placeholder="请选择负责人"
                clearable
                filterable
                remote
                :remote-method="searchUsers"
                :loading="userLoading"
              >
                <el-option
                  v-for="user in userOptions"
                  :key="user.userId"
                  :label="user.userName"
                  :value="user.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, toRefs, getCurrentInstance, nextTick } from 'vue'
import { listInfo, getInfo, delInfo, addInfo, updateInfo } from '@/api/info'
import { listApp } from '@/api/app'
import { listModule } from '@/api/module'
import { pageUser } from '@/api/system/user'
import { parseTime } from '@/utils/ruoyi'

const { proxy } = getCurrentInstance()

const infoList = ref([])
const appOptions = ref([])
const moduleOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const userOptions = ref([])
const userLoading = ref(false)
const apiStatusOptions = ref([
  { label: '开发中', value: '0' },
  { label: '测试中', value: '1' },
  { label: '已发布', value: '2' },
  { label: '已下线', value: '3' }
])
const publishStatusOptions = ref([
  { label: '未发布', value: '0' },
  { label: '已发布', value: '1' },
  { label: '灰度发布', value: '2' }
])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    appId: undefined,
    moduleId: undefined,
    apiCode: undefined,
    apiName: undefined,
    requestUrl: undefined,
    apiStatus: undefined,
    publishStatus: undefined
  },
  rules: {
    appId: [
      { required: true, message: "所属应用不能为空", trigger: "blur" }
    ],
    moduleId: [
      { required: true, message: "所属模块不能为空", trigger: "blur" }
    ],
    apiCode: [
      { required: true, message: "API编码不能为空", trigger: "blur" }
    ],
    apiName: [
      { required: true, message: "API名称不能为空", trigger: "blur" }
    ],
    requestMethod: [
      { required: true, message: "请求方法不能为空", trigger: "blur" }
    ],
    requestUrl: [
      { required: true, message: "请求URL不能为空", trigger: "blur" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询API接口列表 */
function getList() {
  loading.value = true
  listInfo(queryParams.value).then(response => {
    infoList.value = response.data.records || response.data
    total.value = response.data.total || response.data.length
    loading.value = false
  })
}

/** 查询应用列表 */
function getAppList() {
  listApp({}).then(response => {
    appOptions.value = response.data.records || response.data || []
  })
}

/** 查询模块列表 */
function getModuleList() {
  listModule({}).then(response => {
    moduleOptions.value = response.data || []
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    apiId: undefined,
    appId: undefined,
    moduleId: undefined,
    apiCode: undefined,
    apiName: undefined,
    apiDesc: undefined,
    requestMethod: undefined,
    requestUrl: undefined,
    contentType: undefined,
    requestParams: undefined,
    responseParams: undefined,
    requestExample: undefined,
    responseExample: undefined,
    apiStatus: "0",
    publishStatus: "0",
    responsibleUserId: undefined
  }
  // 注释掉这行，因为它可能会干扰对话框的显示
  // proxy.resetForm("infoRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.apiId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加API接口"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _apiId = row.apiId || ids.value
  getInfo(_apiId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改API接口"
  })
}

/** 查看按钮操作 */
function handleView(row) {
  reset()
  const _apiId = row.apiId
  getInfo(_apiId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "查看API接口"
  })
}

/** 提交按钮 */
function submitForm() {
  if (!proxy.$refs["infoRef"]) {
    console.error('表单引用不存在')
    proxy.$modal.msgError("表单引用不存在")
    return
  }
  
  proxy.$refs["infoRef"].validate(valid => {
    if (valid) {
      if (form.value.apiId != undefined) {
        updateInfo(form.value).then(response => {
          // 无论响应码是什么，都先关闭对话框
          open.value = false
          
          if (response.code === 200) {
            proxy.$modal.msgSuccess("修改成功")
            // 延迟刷新列表，确保对话框已关闭
            setTimeout(() => {
              getList()
            }, 500)
          } else {
            proxy.$modal.msgError(response.msg || "修改失败")
            // 即使修改失败，也刷新列表以显示最新状态
            setTimeout(() => {
              getList()
            }, 500)
          }
        }).catch(error => {
          console.error('更新API信息失败:', error)
          // 即使出现异常，也关闭对话框
          open.value = false
          proxy.$modal.msgError("更新失败：" + (error.message || "未知错误"))
          // 刷新列表以显示最新状态
          setTimeout(() => {
            getList()
          }, 500)
        })
      } else {
        addInfo(form.value).then(response => {
          // 无论响应码是什么，都先关闭对话框
          open.value = false
          
          if (response.code === 200) {
            proxy.$modal.msgSuccess("新增成功")
            // 延迟刷新列表，确保对话框已关闭
            setTimeout(() => {
              getList()
            }, 500)
          } else {
            proxy.$modal.msgError(response.msg || "新增失败")
            // 即使新增失败，也刷新列表以显示最新状态
            setTimeout(() => {
              getList()
            }, 500)
          }
        }).catch(error => {
          console.error('新增API信息失败:', error)
          // 即使出现异常，也关闭对话框
          open.value = false
          proxy.$modal.msgError("新增失败：" + (error.message || "未知错误"))
          // 刷新列表以显示最新状态
          setTimeout(() => {
            getList()
          }, 500)
        })
      }
    } else {
      console.log('表单验证失败')
    }
  }).catch(error => {
    console.error('表单验证错误:', error)
    proxy.$modal.msgError("表单验证失败")
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  console.log('handleDelete called with row:', row)
  console.log('ids.value:', ids.value)
  
  const apiIds = row.apiId ? [row.apiId] : ids.value
  console.log('apiIds to delete:', apiIds)
  
  if (apiIds.length === 0) {
    proxy.$modal.msgWarning("请选择要删除的数据")
    return
  }
  
  const apiIdsStr = Array.isArray(apiIds) ? apiIds.join(',') : apiIds
  console.log('apiIdsStr:', apiIdsStr)
  
  proxy.$modal.confirm('是否确认删除API接口编号为"' + apiIdsStr + '"的数据项？').then(() => {
    console.log('User confirmed deletion')
    loading.value = true
    return delInfo(apiIds)
  }).then((response) => {
    console.log('Delete response:', response)
    proxy.$modal.msgSuccess("删除成功")
    getList()
  }).catch((error) => {
    console.error('Delete error:', error)
    proxy.$modal.msgError("删除失败：" + (error.message || "未知错误"))
  }).finally(() => {
    loading.value = false
  })
}

// 直接删除函数 - 简化版删除功能
function directDelete(row) {
  console.log('directDelete called with row:', row)
  
  // 直接使用API ID
  const apiId = row.apiId
  console.log('apiId to delete:', apiId)
  
  if (!apiId) {
    proxy.$modal.msgError("无效的API ID")
    return
  }
  
  // 使用更简单的确认方式
  if (confirm('确定要删除API接口"' + row.apiName + '"吗？')) {
    console.log('User confirmed deletion')
    loading.value = true
    
    // 直接传递API ID，而不是数组
    delInfo(apiId).then((response) => {
      console.log('Delete response:', response)
      proxy.$modal.msgSuccess("删除成功")
      getList()
    }).catch((error) => {
      console.error('Delete error:', error)
      proxy.$modal.msgError("删除失败：" + (error.message || "未知错误"))
    }).finally(() => {
      loading.value = false
    })
  }
}

// 批量删除函数
function batchDelete() {
  console.log('batchDelete called')
  console.log('ids.value:', ids.value)
  
  if (ids.value.length === 0) {
    proxy.$modal.msgWarning("请选择要删除的数据")
    return
  }
  
  const apiIdsStr = ids.value.join(',')
  console.log('apiIdsStr:', apiIdsStr)
  
  // 使用更简单的确认方式
  if (confirm('确定要删除选中的' + ids.value.length + '个API接口吗？')) {
    console.log('User confirmed batch deletion')
    loading.value = true
    
    // 传递ID数组
    delInfo(ids.value).then((response) => {
      console.log('Batch delete response:', response)
      proxy.$modal.msgSuccess("删除成功")
      getList()
    }).catch((error) => {
      console.error('Batch delete error:', error)
      proxy.$modal.msgError("删除失败：" + (error.message || "未知错误"))
    }).finally(() => {
      loading.value = false
    })
  }
}

/** 获取请求方法类型 */
function getMethodType(method) {
  if (method === 'GET') {
    return 'success'
  } else if (method === 'POST') {
    return 'primary'
  } else if (method === 'PUT') {
    return 'warning'
  } else if (method === 'DELETE') {
    return 'danger'
  }
  return 'info'
}

/** 搜索用户 */
function searchUsers(query) {
  if (query !== '') {
    userLoading.value = true
    // 使用分页接口而不是列表接口
    pageUser({ pageNum: 1, pageSize: 20, userName: query }).then(response => {
      userOptions.value = response.data.records || response.data || []
      userLoading.value = false
    })
  } else {
    userOptions.value = []
  }
}

onMounted(() => {
  getList()
  getAppList()
  getModuleList()
  // 初始化用户列表
  pageUser({ pageNum: 1, pageSize: 20 }).then(response => {
    userOptions.value = response.data.records || response.data || []
  })
})

// 强制关闭对话框函数
function forceCloseDialog() {
  console.log('forceCloseDialog called')
  open.value = false
  reset()
  
  // 使用多种方法确保对话框关闭
  setTimeout(() => {
    open.value = false
    // 尝试直接操作DOM
    const dialog = document.querySelector('.el-dialog')
    if (dialog) {
      dialog.style.display = 'none'
    }
    // 尝试使用Element Plus的实例方法
    const dialogInstance = proxy.$refs.infoDialog
    if (dialogInstance && dialogInstance.close) {
      dialogInstance.close()
    }
  }, 100)
  
  // 强制刷新列表
  setTimeout(() => {
    getList()
  }, 200)
}

// 对话框关闭事件处理函数
function handleDialogClose() {
  open.value = false
  reset()
}
</script>