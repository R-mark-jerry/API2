<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号码" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入手机号码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="用户状态" clearable>
          <el-option
            v-for="dict in statusOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="dateRange">
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
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户编号" align="center" prop="userId" />
      <el-table-column label="用户名称" align="center" prop="userName" :show-overflow-tooltip="true" />
      <el-table-column label="用户昵称" align="center" prop="nickName" :show-overflow-tooltip="true" />
      <el-table-column label="部门" align="center" prop="deptName" :show-overflow-tooltip="true" />
      <el-table-column label="手机号码" align="center" prop="phonenumber" width="120" />
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
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template #default="scope">
          <el-button
            type="text"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:user:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="Delete"
            @click="directDelete(scope.row)"
          >删除</el-button>
          <el-dropdown @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['system:user:resetPwd', 'system:user:role']">
            <el-button type="text" icon="More">
              更多
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="handleResetPwd" v-hasPermi="['system:user:resetPwd']">重置密码</el-dropdown-item>
                <el-dropdown-item command="handleAuthRole" v-hasPermi="['system:user:role']">分配角色</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
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

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body ref="userDialog">
      <el-form ref="userRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归属部门" prop="deptId">
              <el-tree-select
                v-model="form.deptId"
                :data="deptOptions"
                :props="{ value: 'id', label: 'label', children: 'children' }"
                value-key="id"
                placeholder="选择归属部门"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phonenumber">
              <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名称" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户密码" prop="password" v-if="form.userId == undefined">
              <el-input v-model="form.password" placeholder="请输入用户密码" type="password" maxlength="20" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-select v-model="form.sex" placeholder="请选择">
                <el-option
                  v-for="dict in sexOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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

    <!-- 用户导入对话框 -->
    <el-dialog title="用户导入" v-model="upload.open" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, toRefs, getCurrentInstance, nextTick } from 'vue'
import { parseTime } from '@/utils/ruoyi'
import { getToken } from '@/utils/auth'
import { pageUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus, deptTreeSelect } from '@/api/system/user'

const { proxy } = getCurrentInstance()

const userList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const dateRange = ref([])
const deptName = ref("")
const deptOptions = ref([])
const initPassword = ref(undefined)
const postOptions = ref([])
const roleOptions = ref([])

// 上传参数
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/system/user/importData"
})

const statusOptions = ref([
  { label: '正常', value: '0' },
  { label: '停用', value: '1' }
])

const sexOptions = ref([
  { label: '男', value: '0' },
  { label: '女', value: '1' }
])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userName: undefined,
    phonenumber: undefined,
    status: undefined,
    deptId: undefined
  },
  rules: {
    userName: [
      { required: true, message: "用户名称不能为空", trigger: "blur" },
      { min: 2, max: 20, message: "用户名称长度在 2 到 20 个字符", trigger: "blur" }
    ],
    nickName: [
      { required: true, message: "用户昵称不能为空", trigger: "blur" }
    ],
    password: [
      { required: true, message: "用户密码不能为空", trigger: "blur" },
      { min: 5, max: 20, message: "用户密码长度在 5 到 20 个字符", trigger: "blur" }
    ],
    email: [
      {
        type: "email",
        message: "请输入正确的邮箱地址",
        trigger: ["blur", "change"]
      }
    ],
    phonenumber: [
      {
        pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
        message: "请输入正确的手机号码",
        trigger: "blur"
      }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询用户列表 */
function getList() {
  loading.value = true
  pageUser(queryParams.value).then(response => {
    // 处理后端返回的数据格式
    if (response.code === 200) {
      // 后端返回的是MyBatis Plus的分页对象，包含records和total字段
      if (response.data && typeof response.data === 'object') {
        // 直接从response.data中获取records和total
        userList.value = response.data.records || []
        total.value = response.data.total || 0
      } else {
        // 兜底处理
        userList.value = []
        total.value = 0
      }
    } else {
      console.error('获取用户列表失败:', response.msg || response.message)
      proxy.$modal.msgError(response.msg || response.message || '获取用户列表失败')
    }
    loading.value = false
  }).catch(error => {
    console.error('获取用户列表失败:', error)
    proxy.$modal.msgError('获取用户列表失败，请稍后重试')
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
    userId: undefined,
    deptId: undefined,
    userName: undefined,
    nickName: undefined,
    password: undefined,
    phonenumber: undefined,
    email: undefined,
    sex: undefined,
    status: "0",
    remark: undefined,
    postIds: [],
    roleIds: []
  }
  // 注释掉这行，因为它可能会干扰对话框的显示
  // proxy.resetForm("userRef")
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

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.userId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  // 新增用户时不需要获取用户信息，直接打开对话框
  open.value = true
  title.value = "添加用户"
  // 可以在这里设置默认密码
  form.value.password = "123456" // 或者从某个配置获取默认密码
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const userId = row.userId || ids.value
  getUser(userId).then(response => {
    form.value = response.data
    postOptions.value = response.posts
    roleOptions.value = response.roles
    form.value.postIds = response.postIds
    form.value.roleIds = response.roleIds
    open.value = true
    title.value = "修改用户"
  })
}

/** 提交按钮 */
function submitForm() {
  if (!proxy.$refs["userRef"]) {
    console.error('表单引用不存在')
    proxy.$modal.msgError("表单引用不存在")
    return
  }
  
  proxy.$refs["userRef"].validate(valid => {
    if (valid) {
      if (form.value.userId != undefined) {
        updateUser(form.value).then(response => {
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
          console.error('更新用户失败:', error)
          // 即使出现异常，也关闭对话框
          open.value = false
          proxy.$modal.msgError("更新失败：" + (error.message || "未知错误"))
          // 刷新列表以显示最新状态
          setTimeout(() => {
            getList()
          }, 500)
        })
      } else {
        addUser(form.value).then(response => {
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
          console.error('新增用户失败:', error)
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
  
  const userIds = row.userId ? [row.userId] : ids.value
  console.log('userIds to delete:', userIds)
  
  if (userIds.length === 0) {
    proxy.$modal.msgWarning("请选择要删除的数据")
    return
  }
  
  const userIdsStr = Array.isArray(userIds) ? userIds.join(',') : userIds
  console.log('userIdsStr:', userIdsStr)
  
  proxy.$modal.confirm('是否确认删除用户编号为"' + userIdsStr + '"的数据项？').then(() => {
    console.log('User confirmed deletion')
    loading.value = true
    return delUser(userIds)
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

/** 用户状态修改 */
function handleStatusChange(row) {
  let text = row.status === "0" ? "启用" : "停用"
  proxy.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗？').then(function() {
    return changeUserStatus(row.userId, row.status)
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(function() {
    row.status = row.status === "0" ? "1" : "0"
  })
}

/** 更多操作 */
function handleCommand(command, row) {
  switch (command) {
    case "handleResetPwd":
      handleResetPwd(row)
      break
    case "handleAuthRole":
      handleAuthRole(row)
      break
    default:
      break
  }
}

/** 重置密码按钮操作 */
function handleResetPwd(row) {
  proxy.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    closeOnClickModal: false,
    inputPattern: /^.{5,20}$/,
    inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
  }).then(({ value }) => {
    resetUserPwd(row.userId, value).then(response => {
      proxy.$modal.msgSuccess("修改成功，新密码是：" + value)
    })
  }).catch(() => {})
}

/** 分配角色操作 */
function handleAuthRole(row) {
  const userId = row.userId
  proxy.$router.push("/system/user-auth/role/" + userId)
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("system/user/export", {
    ...queryParams.value,
  }, `user_${new Date().getTime()}.xlsx`)
}

/** 导入按钮操作 */
function handleImport() {
  upload.title = "用户导入"
  upload.open = true
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("system/user/importTemplate", {
  }, `user_template_${new Date().getTime()}.xlsx`)
}

// 文件上传中处理
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true
}

// 文件上传成功处理
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false
  upload.isUploading = false
  proxy.$refs["uploadRef"].handleRemove(file)
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
  getList()
}

// 提交上传文件
function submitFileForm() {
  proxy.$refs["uploadRef"].submit()
}


onMounted(() => {
  getList()
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
    const dialogInstance = proxy.$refs.userDialog
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

// 直接删除函数 - 简化版删除功能
function directDelete(row) {
  console.log('directDelete called with row:', row)
  
  // 直接使用用户ID
  const userId = row.userId
  console.log('userId to delete:', userId)
  
  if (!userId) {
    proxy.$modal.msgError("无效的用户ID")
    return
  }
  
  // 使用更简单的确认方式
  if (confirm('确定要删除用户"' + row.userName + '"吗？')) {
    console.log('User confirmed deletion')
    loading.value = true
    
    // 直接传递用户ID，而不是数组
    delUser(userId).then((response) => {
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
  
  const userIdsStr = ids.value.join(',')
  console.log('userIdsStr:', userIdsStr)
  
  // 使用更简单的确认方式
  if (confirm('确定要删除选中的' + ids.value.length + '个用户吗？')) {
    console.log('User confirmed batch deletion')
    loading.value = true
    
    // 传递ID数组
    delUser(ids.value).then((response) => {
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

</script>