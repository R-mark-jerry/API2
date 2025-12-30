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
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
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
            @click="directDelete(scope.row)"
            v-hasPermi="['api:module:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改API模块对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body ref="moduleDialog">
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
import { ref, onMounted, reactive, toRefs, getCurrentInstance, nextTick } from 'vue'
import { listModule, getModule, delModule, addModule, updateModule, treeModule } from '@/api/module'
import { listApp } from '@/api/app'
import { pageUser } from '@/api/system/user'
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
const userOptions = ref([])
const userLoading = ref(false)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)

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
    // 直接从response.data中获取数据，然后处理树形结构
    moduleList.value = handleTree(response.data.records || response.data || [], 'moduleId')
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
    appOptions.value = response.data.records || response.data || []
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
    orderNum: 0,
    responsibleUserId: undefined
  }
  // 注释掉这行，因为它可能会干扰对话框的显示
  // proxy.resetForm("moduleRef")
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
  if (!proxy.$refs["moduleRef"]) {
    console.error('表单引用不存在')
    proxy.$modal.msgError("表单引用不存在")
    return
  }
  
  proxy.$refs["moduleRef"].validate(valid => {
    if (valid) {
      if (form.value.moduleId != undefined) {
        updateModule(form.value).then(response => {
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
          console.error('更新模块失败:', error)
          // 即使出现异常，也关闭对话框
          open.value = false
          proxy.$modal.msgError("更新失败：" + (error.message || "未知错误"))
          // 刷新列表以显示最新状态
          setTimeout(() => {
            getList()
          }, 500)
        })
      } else {
        addModule(form.value).then(response => {
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
          console.error('新增模块失败:', error)
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
  
  const moduleIds = row.moduleId ? [row.moduleId] : ids.value
  console.log('moduleIds to delete:', moduleIds)
  
  if (moduleIds.length === 0) {
    proxy.$modal.msgWarning("请选择要删除的数据")
    return
  }
  
  const moduleIdsStr = Array.isArray(moduleIds) ? moduleIds.join(',') : moduleIds
  console.log('moduleIdsStr:', moduleIdsStr)
  
  proxy.$modal.confirm('是否确认删除模块编号为"' + moduleIdsStr + '"的数据项？').then(() => {
    console.log('User confirmed deletion')
    loading.value = true
    return delModule(moduleIds)
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
  
  // 直接使用模块ID
  const moduleId = row.moduleId
  console.log('moduleId to delete:', moduleId)
  
  if (!moduleId) {
    proxy.$modal.msgError("无效的模块ID")
    return
  }
  
  // 使用更简单的确认方式
  if (confirm('确定要删除模块"' + row.moduleName + '"吗？')) {
    console.log('User confirmed deletion')
    loading.value = true
    
    // 直接传递模块ID，而不是数组
    delModule(moduleId).then((response) => {
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
  
  const moduleIdsStr = ids.value.join(',')
  console.log('moduleIdsStr:', moduleIdsStr)
  
  // 使用更简单的确认方式
  if (confirm('确定要删除选中的' + ids.value.length + '个模块吗？')) {
    console.log('User confirmed batch deletion')
    loading.value = true
    
    // 传递ID数组
    delModule(ids.value).then((response) => {
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

/** 展开/折叠操作 */
function handleToggleExpand() {
  isExpandAll.value = !isExpandAll.value
  refreshTable()
}

/** 刷新表格 */
function refreshTable() {
  getList()
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
  getModuleTree()
  getAppList()
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
    const dialogInstance = proxy.$refs.moduleDialog
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

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.moduleId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}
</script>