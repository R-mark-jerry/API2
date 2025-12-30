<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用名称" prop="appName">
        <el-input
          v-model="queryParams.appName"
          placeholder="请输入应用名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="权限类型" prop="permissionType">
        <el-select v-model="queryParams.permissionType" placeholder="权限类型" clearable>
          <el-option
            v-for="dict in permissionTypeOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="权限状态" clearable>
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
          @click="batchDelete"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="permissionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="权限编号" align="center" prop="permissionId" />
      <el-table-column label="应用名称" align="center" prop="appName" :show-overflow-tooltip="true" />
      <el-table-column label="权限类型" align="center" prop="permissionType">
        <template #default="scope">
          <dict-tag :options="permissionTypeOptions" :value="scope.row.permissionType"/>
        </template>
      </el-table-column>
      <el-table-column label="权限标识" align="center" prop="permissionCode" :show-overflow-tooltip="true" />
      <el-table-column label="权限名称" align="center" prop="permissionName" :show-overflow-tooltip="true" />
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
            v-hasPermi="['api:permission:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="Delete"
            @click="directDelete(scope.row)"
            v-hasPermi="['api:permission:remove']"
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

    <!-- 添加或修改API权限对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body ref="permissionDialog">
      <el-form ref="permissionRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="应用ID" prop="appId">
          <el-input v-model="form.appId" placeholder="请输入应用ID" />
        </el-form-item>
        <el-form-item label="权限类型" prop="permissionType">
          <el-select v-model="form.permissionType" placeholder="请选择权限类型">
            <el-option
              v-for="dict in permissionTypeOptions"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="权限标识" prop="permissionCode">
          <el-input v-model="form.permissionCode" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="form.permissionName" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限描述" prop="permissionDesc">
          <el-input v-model="form.permissionDesc" type="textarea" placeholder="请输入权限描述" />
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
import { ref, onMounted, reactive, toRefs, getCurrentInstance, nextTick } from 'vue'
import { parseTime } from '@/utils/ruoyi'
import { pagePermission, getPermission, delPermission, addPermission, updatePermission, changePermissionStatus } from '@/api/permission'
import { pageUser } from '@/api/system/user'

const { proxy } = getCurrentInstance()

const permissionList = ref([])
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
const permissionTypeOptions = ref([
  { label: '应用权限', value: '0' },
  { label: '接口权限', value: '1' },
  { label: '模块权限', value: '2' }
])
const statusOptions = ref([
  { label: '正常', value: '0' },
  { label: '停用', value: '1' }
])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    appName: undefined,
    permissionType: undefined,
    status: undefined
  },
  rules: {
    appId: [
      { required: true, message: "应用ID不能为空", trigger: "blur" }
    ],
    permissionType: [
      { required: true, message: "权限类型不能为空", trigger: "blur" }
    ],
    permissionCode: [
      { required: true, message: "权限标识不能为空", trigger: "blur" }
    ],
    permissionName: [
      { required: true, message: "权限名称不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "blur" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询API权限列表 */
function getList() {
  loading.value = true
  pagePermission(queryParams.value).then(response => {
    // 直接从response.data中获取records和total
    permissionList.value = response.data.records || []
    total.value = response.data.total || 0
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
    permissionId: undefined,
    appId: undefined,
    permissionType: undefined,
    permissionCode: undefined,
    permissionName: undefined,
    permissionDesc: undefined,
    status: "0",
    responsibleUserId: undefined
  }
  // 注释掉这行，因为它可能会干扰对话框的显示
  // proxy.resetForm("permissionRef")
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
  ids.value = selection.map(item => item.permissionId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加API权限"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _permissionId = row.permissionId || ids.value
  getPermission(_permissionId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改API权限"
  })
}

/** 提交按钮 */
function submitForm() {
  if (!proxy.$refs["permissionRef"]) {
    console.error('表单引用不存在')
    proxy.$modal.msgError("表单引用不存在")
    return
  }
  
  proxy.$refs["permissionRef"].validate(valid => {
    if (valid) {
      if (form.value.permissionId != undefined) {
        updatePermission(form.value).then(response => {
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
          console.error('更新权限失败:', error)
          // 即使出现异常，也关闭对话框
          open.value = false
          proxy.$modal.msgError("更新失败：" + (error.message || "未知错误"))
          // 刷新列表以显示最新状态
          setTimeout(() => {
            getList()
          }, 500)
        })
      } else {
        addPermission(form.value).then(response => {
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
          console.error('新增权限失败:', error)
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
  
  const permissionIds = row.permissionId ? [row.permissionId] : ids.value
  console.log('permissionIds to delete:', permissionIds)
  
  if (permissionIds.length === 0) {
    proxy.$modal.msgWarning("请选择要删除的数据")
    return
  }
  
  const permissionIdsStr = Array.isArray(permissionIds) ? permissionIds.join(',') : permissionIds
  console.log('permissionIdsStr:', permissionIdsStr)
  
  proxy.$modal.confirm('是否确认删除API权限编号为"' + permissionIdsStr + '"的数据项？').then(() => {
    console.log('User confirmed deletion')
    loading.value = true
    return delPermission(permissionIds)
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
  
  // 直接使用权限ID
  const permissionId = row.permissionId
  console.log('permissionId to delete:', permissionId)
  
  if (!permissionId) {
    proxy.$modal.msgError("无效的权限ID")
    return
  }
  
  // 使用更简单的确认方式
  if (confirm('确定要删除API权限"' + row.permissionName + '"吗？')) {
    console.log('User confirmed deletion')
    loading.value = true
    
    // 直接传递权限ID，而不是数组
    delPermission(permissionId).then((response) => {
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
  
  const permissionIdsStr = ids.value.join(',')
  console.log('permissionIdsStr:', permissionIdsStr)
  
  // 使用更简单的确认方式
  if (confirm('确定要删除选中的' + ids.value.length + '个API权限吗？')) {
    console.log('User confirmed batch deletion')
    loading.value = true
    
    // 传递ID数组
    delPermission(ids.value).then((response) => {
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

/** 状态修改 */
function handleStatusChange(row) {
  let text = row.status === "0" ? "启用" : "停用"
  proxy.$modal.confirm('确认要"' + text + '""' + row.permissionName + '"权限吗？').then(function() {
    return changePermissionStatus(row).then(() => {
      proxy.$modal.msgSuccess(text + "成功")
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

onMounted(() => {
  getList()
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
    const dialogInstance = proxy.$refs.permissionDialog
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