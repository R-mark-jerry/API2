import { defineStore } from 'pinia'
import { login, logout, getUserInfo as fetchUserInfo } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userId: null,
    userName: '',
    nickName: '',
    avatar: '',
    email: '',
    phone: '',
    permissions: []
  }),

  getters: {
    isLogin: (state) => !!state.token,
    userInfo: (state) => ({
      userId: state.userId,
      userName: state.userName,
      nickName: state.nickName,
      avatar: state.avatar,
      email: state.email,
      phone: state.phone
    })
  },

  actions: {
    // 登录
    login(loginForm) {
      return new Promise((resolve, reject) => {
        login(loginForm)
          .then(response => {
            const { data } = response
            this.token = data.token
            this.userId = data.userId
            this.userName = data.userName
            this.nickName = data.nickName
            this.avatar = data.avatar
            this.email = data.email
            this.phone = data.phone
            
            setToken(data.token)
            resolve(response)
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 登出
    logout() {
      return new Promise((resolve, reject) => {
        logout()
          .then(() => {
            this.token = ''
            this.userId = null
            this.userName = ''
            this.nickName = ''
            this.avatar = ''
            this.email = ''
            this.phone = ''
            this.permissions = []
            
            removeToken()
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 获取用户信息
    getUserInfo() {
      return new Promise((resolve, reject) => {
        fetchUserInfo()
          .then(response => {
            const { data } = response
            
            if (!data) {
              reject('获取用户信息失败，请重新登录')
            }
            
            this.userId = data.userId
            this.userName = data.userName
            this.nickName = data.nickName
            this.avatar = data.avatar
            this.email = data.email
            this.phone = data.phone
            
            // 这里可以根据后端返回的权限信息设置permissions
            // this.permissions = data.permissions || []
            
            resolve(data)
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 重置用户信息
    resetUser() {
      this.token = ''
      this.userId = null
      this.userName = ''
      this.nickName = ''
      this.avatar = ''
      this.email = ''
      this.phone = ''
      this.permissions = []
      
      removeToken()
    },

    // 从本地存储恢复用户信息
    restoreUserFromStorage() {
      const token = getToken()
      if (token) {
        this.token = token
      }
    },

    // 更新用户信息
    updateUserInfo(userInfo) {
      this.userId = userInfo.userId || this.userId
      this.userName = userInfo.userName || this.userName
      this.nickName = userInfo.nickName || this.nickName
      this.avatar = userInfo.avatar || this.avatar
      this.email = userInfo.email || this.email
      this.phone = userInfo.phone || this.phone
    }
  }
})