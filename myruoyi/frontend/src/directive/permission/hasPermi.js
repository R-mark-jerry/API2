import { useUserStore } from '@/stores/user'

export default {
  mounted(el, binding) {
    const { value } = binding
    const all_permission = "*:*:*"
    const userStore = useUserStore()
    const permissions = userStore.permissions || []

    // 调试信息
    console.log('权限检查:', {
      requiredPermissions: value,
      userPermissions: permissions,
      hasAllPermission: permissions.includes(all_permission)
    })

    if (value && value instanceof Array && value.length > 0) {
      const hasAllPermission = permissions.includes(all_permission)
      
      const hasPermissions = value.some(permission => {
        return hasAllPermission || permissions.includes(permission)
      })

      if (!hasPermissions) {
        console.warn('权限不足，需要权限:', value, '当前权限:', permissions)
        el.parentNode && el.parentNode.removeChild(el)
      } else {
        console.log('权限检查通过:', value)
      }
    } else {
      throw new Error(`请设置操作权限标签值`)
    }
  }
}