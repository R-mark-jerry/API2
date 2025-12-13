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
          @click="handleDelete"
        >删除</el-button>
      </el-col>
    </el-row>

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
          <el-input v-model="form.ownerId" placeholder="请输入负责人ID" />
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
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, toRefs, getCurrentInstance } from 'vue'
import { listApp, getApp, delApp, addApp, updateApp, changeAppStatus } from '@/api/app'
import { parseTime } from '@/utils/ruoyi'

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
  proxy.resetForm("appRef")
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
        })
      } else {
        addApp(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _appIds = row.appId || ids.value
  proxy.$modal.confirm('是否确认删除API应用编号为"' + _appIds + '"的数据项？').then(function() {
    return delApp(_appIds).then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
  }).catch(() => {})
}

/** 状态修改 */
function handleStatusChange(row) {
  let text = row.status === "0" ? "启用" : "停用"
  proxy.$modal.confirm('确认要"' + text + '""' + row.appName + '"应用吗？').then(function() {
    return changeAppStatus(row).then(() => {
      proxy.$modal.msgSuccess(text + "成功")
    })
  }).catch(function() {
    row.status = row.status === "0" ? "1" : "0"
  })
}

onMounted(() => {
  getList()
})
</script>