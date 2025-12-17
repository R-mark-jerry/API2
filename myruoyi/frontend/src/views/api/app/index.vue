<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用编码" prop="appCode">
        <el-input
          v-model="queryParams.appCode"
          placeholder="请输入应用编码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="应用名称" prop="appName">
        <el-input
          v-model="queryParams.appName"
          placeholder="请输入应用名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="应用状态" clearable>
          <el-option
            v-for="dict in statusOptions"
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
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
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Upload"
          @click="handleImport"
        >导入</el-button>
      </el-col>
    </el-row>

    <!-- 导入对话框 -->
    <el-dialog title="导入API应用" v-model="importOpen" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".csv, .xls, .xlsx"
        :headers="uploadHeaders"
        :action="uploadUrl"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">
            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的应用数据
            <span>仅允许导入xls、xlsx或csv格式文件。</span>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.file = []">取 消</el-button>
          <el-button type="info" @click="downloadTemplate">
            下载模板
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-table v-loading="loading" :data="appList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="应用编号" align="center" prop="appId" />
      <el-table-column label="应用编码" align="center" prop="appCode" :show-overflow-tooltip="true" />
      <el-table-column label="应用名称" align="center" prop="appName" :show-overflow-tooltip="true" />
      <el-table-column label="应用描述" align="center" prop="appDesc" :show-overflow-tooltip="true" />
      <el-table-column label="应用版本" align="center" prop="appVersion" />
      <el-table-column label="负责人" align="center" prop="ownerName" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
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
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['api:app:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['api:app:remove']"
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

    <!-- 添加或修改API应用对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="appRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="应用编码" prop="appCode">
          <el-input v-model="form.appCode" placeholder="请输入应用编码" />
        </el-form-item>
        <el-form-item label="应用名称" prop="appName">
          <el-input v-model="form.appName" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="应用描述" prop="appDesc">
          <el-input v-model="form.appDesc" type="textarea" placeholder="请输入应用描述" />
        </el-form-item>
        <el-form-item label="应用版本" prop="appVersion">
          <el-input v-model="form.appVersion" placeholder="请输入应用版本" />
        </el-form-item>
        <el-form-item label="负责人" prop="ownerId">
          <el-select
            v-model="form.ownerId"
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
              :label="user.nickName + '(' + user.userName + ')'"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, toRefs, getCurrentInstance } from 'vue'
import { listApp, getApp, delApp, addApp, updateApp, changeAppStatus, exportApp, importApp, importTemplate } from '@/api/app'
import { pageUser } from '@/api/system/user'
import { parseTime } from '@/utils/ruoyi'

console.log('App component script setup executed')

const { proxy } = getCurrentInstance()

const appList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const statusOptions = ref([
  { label: '正常', value: '0' },
  { label: '停用', value: '1' }
])
const userOptions = ref([])
const userLoading = ref(false)
const importOpen = ref(false)
const upload = reactive({
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的应用数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + '/app/import'
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    appCode: undefined,
    appName: undefined,
    status: undefined
  },
  rules: {
    appCode: [
      { required: true, message: "应用编码不能为空", trigger: "blur" }
    ],
    appName: [
      { required: true, message: "应用名称不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "blur" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询API应用列表 */
function getList() {
  loading.value = true
  listApp(queryParams.value).then(response => {
    appList.value = response.data.records || response.data
    total.value = response.data.total || response.data.length
    loading.value = false
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
    appId: undefined,
    appCode: undefined,
    appName: undefined,
    appDesc: undefined,
    appVersion: undefined,
    ownerId: undefined,
    status: "0"
  }
  // 注释掉这行，因为它可能会干扰对话框的显示
  // proxy.resetForm("appRef")
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
  ids.value = selection.map(item => item.appId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加API应用"
}


/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _appId = row.appId || ids.value
  getApp(_appId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改API应用"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["appRef"].validate(valid => {
    if (valid) {
      if (form.value.appId != undefined) {
        updateApp(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        }).catch(error => {
          console.error('更新应用失败:', error)
          proxy.$modal.msgError("更新失败：" + (error.message || "未知错误"))
        })
      } else {
        addApp(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        }).catch(error => {
          console.error('新增应用失败:', error)
          proxy.$modal.msgError("新增失败：" + (error.message || "未知错误"))
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _appIds = row.appId || ids.value
  const appNames = row.appName || appList.value.filter(item => _appIds.includes(item.appId)).map(item => item.appName).join(', ')
  proxy.$modal.confirm('是否确认删除应用"' + appNames + '"？删除后不可恢复！').then(function() {
    loading.value = true
    return delApp(_appIds).then(() => {
      proxy.$modal.msgSuccess("删除成功")
      getList()
    }).finally(() => {
      loading.value = false
    })
  }).catch(() => {})
}

/** 状态修改 */
function handleStatusChange(row) {
  let text = row.status === "0" ? "启用" : "停用"
  let confirmText = row.status === "0" ?
    "确认要停用应用\"" + row.appName + "\"吗？停用后应用将不可访问。" :
    "确认要启用应用\"" + row.appName + "\"吗？启用后应用将恢复正常访问。"
    
  proxy.$modal.confirm(confirmText).then(function() {
    loading.value = true
    return changeAppStatus(row).then(() => {
      proxy.$modal.msgSuccess(text + "成功")
    }).finally(() => {
      loading.value = false
    })
  }).catch(function() {
    row.status = row.status === "0" ? "1" : "0"
  })
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

/** 导出按钮操作 */
function handleExport() {
  proxy.$modal.confirm('是否确认导出所有API应用数据项？').then(() => {
    loading.value = true
    return exportApp(queryParams.value)
  }).then(response => {
    const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'API应用数据.csv'
    a.click()
    window.URL.revokeObjectURL(url)
    proxy.$modal.msgSuccess("导出成功")
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

/** 导入按钮操作 */
function handleImport() {
  importOpen.value = true
  upload.updateSupport = false
  upload.isUploading = false
}

/** 下载模板操作 */
function downloadTemplate() {
  loading.value = true
  importTemplate().then(response => {
    const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'API应用导入模板.csv'
    a.click()
    window.URL.revokeObjectURL(url)
    proxy.$modal.msgSuccess("模板下载成功")
    loading.value = false
  }).catch(error => {
    proxy.$modal.msgError("模板下载失败：" + (error.message || "未知错误"))
    loading.value = false
  })
}

/** 文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true
}

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  importOpen.value = false
  upload.isUploading = false
  getList()
  
  if (response.code === 200) {
    proxy.$modal.msgSuccess("导入成功")
    if (response.data && response.data.successCount > 0) {
      proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
        "导入完成！<br/>" +
        "成功导入：" + response.data.successCount + " 条<br/>" +
        "更新数据：" + response.data.updateCount + " 条<br/>" +
        "失败数据：" + response.data.failCount + " 条<br/>" +
        (response.data.failList && response.data.failList.length > 0 ?
          "失败详情：<br/>" + response.data.failList.join('<br/>') : '') +
        "</div>", "导入结果", { dangerouslyUseHTMLString: true })
    }
  } else {
    proxy.$modal.msgError(response.msg || "导入失败")
  }
}

/** 提交上传文件 */
function submitFileForm() {
  const files = proxy.$refs.uploadRef.uploadFiles
  if (!files || files.length === 0) {
    proxy.$modal.msgWarning("请选择要导入的文件")
    return
  }
  
  const file = files[0]
  const fileExt = file.name.substring(file.name.lastIndexOf('.')).toLowerCase()
  if (!['.csv', '.xls', '.xlsx'].includes(fileExt)) {
    proxy.$modal.msgError("文件格式错误，请上传CSV、XLS或XLSX格式的文件")
    return
  }
  
  upload.isUploading = true
  proxy.$refs.uploadRef.submit()
}

onMounted(() => {
  getList()
  // 初始化用户列表
  pageUser({ pageNum: 1, pageSize: 20 }).then(response => {
    userOptions.value = response.data.records || response.data || []
  })
})
</script>