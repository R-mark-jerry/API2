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
      <el-form-item label="模块编码" prop="moduleCode">
        <el-input
          v-model="queryParams.moduleCode"
          placeholder="请输入模块编码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模块名称" prop="moduleName">
        <el-input
          v-model="queryParams.moduleName"
          placeholder="请输入模块名称"
          clearable
          @keyup.enter="handleQuery"
        />
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
          type="info"
          plain
          icon="Sort"
          @click="handleToggleExpand"
        >展开/折叠</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="moduleList"
      row-key="moduleId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="moduleCode" label="模块编码" :show-overflow-tooltip="true" />
      <el-table-column prop="moduleName" label="模块名称" :show-overflow-tooltip="true" />
      <el-table-column prop="orderNum" label="排序" width="100" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            type="text"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['api:module:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="Plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['api:module:add']"
          >新增</el-button>
          <el-button
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['api:module:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改API模块对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="moduleRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="上级模块" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="moduleOptions"
            :props="{ value: 'moduleId', label: 'moduleName', children: 'children' }"
            value-key="moduleId"
            placeholder="选择上级模块"
            check-strictly
          />
        </el-form-item>
        <el-form-item label="模块编码" prop="moduleCode">
          <el-input v-model="form.moduleCode" placeholder="请输入模块编码" />
        </el-form-item>
        <el-form-item label="模块名称" prop="moduleName">
          <el-input v-model="form.moduleName" placeholder="请输入模块名称" />
        </el-form-item>
        <el-form-item label="显示排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="模块描述" prop="moduleDesc">
          <el-input v-model="form.moduleDesc" type="textarea" placeholder="请输入模块描述" />
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
import { listModule, getModule, delModule, addModule, updateModule, treeModule } from '@/api/module'
import { listApp } from '@/api/app'
import { handleTree } from '@/utils/ruoyi'

const { proxy } = getCurrentInstance()

const moduleList = ref([])
const moduleOptions = ref([])
const appOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const isExpandAll = ref(false)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    appId: undefined,
    moduleCode: undefined,
    moduleName: undefined
  },
  rules: {
    parentId: [
      { required: true, message: "上级模块不能为空", trigger: "blur" }
    ],
    moduleCode: [
      { required: true, message: "模块编码不能为空", trigger: "blur" }
    ],
    moduleName: [
      { required: true, message: "模块名称不能为空", trigger: "blur" }
    ],
    orderNum: [
      { required: true, message: "显示排序不能为空", trigger: "blur" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询API模块列表 */
function getList() {
  loading.value = true
  listModule(queryParams.value).then(response => {
    moduleList.value = handleTree(response.data, 'moduleId')
    loading.value = false
  })
}

/** 查询模块下拉树结构 */
function getModuleTree() {
  treeModule().then(response => {
    moduleOptions.value = []
    const menu = { moduleId: 0, moduleName: '主类目', children: [] }
    menu.children = response.data
    moduleOptions.value.push(menu)
  })
}

/** 查询应用列表 */
function getAppList() {
  listApp({}).then(response => {
    appOptions.value = response.data
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
    moduleId: undefined,
    appId: undefined,
    parentId: 0,
    moduleCode: undefined,
    moduleName: undefined,
    moduleDesc: undefined,
    orderNum: 0
  }
  proxy.resetForm("moduleRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset()
  if (row != null && row.moduleId) {
    form.value.parentId = row.moduleId
    form.value.appId = row.appId
  } else {
    form.value.parentId = 0
  }
  open.value = true
  title.value = "添加API模块"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  getModule(row.moduleId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改API模块"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["moduleRef"].validate(valid => {
    if (valid) {
      if (form.value.moduleId != undefined) {
        updateModule(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addModule(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除名称为"' + row.moduleName + '"的数据项？').then(function() {
    return delModule(row.moduleId).then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
  }).catch(() => {})
}

/** 展开/折叠操作 */
function handleToggleExpand() {
  isExpandAll.value = !isExpandAll.value
  refreshTable()
}

/** 刷新表格 */
function refreshTable() {
  getList()
}

onMounted(() => {
  getList()
  getModuleTree()
  getAppList()
})
</script>