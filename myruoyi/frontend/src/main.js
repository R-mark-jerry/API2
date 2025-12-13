import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

import App from './App.vue'
import router from './router'
import './styles/index.scss'

// 全局组件
import DictTag from '@/components/DictTag/index.vue'
import Pagination from '@/components/Pagination/index.vue'

// 全局指令
import directive from './directive'

const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册全局组件
app.component('DictTag', DictTag)
app.component('Pagination', Pagination)

// 注册全局指令
app.use(directive)

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: zhCn,
})

app.mount('#app')