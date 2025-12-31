<template>
  <component :is="type" v-bind="linkProps(to)" @click="handleClick">
    <slot />
  </component>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { isExternal } from '@/utils/validate'

const props = defineProps({
  to: {
    type: String,
    required: true
  }
})

const router = useRouter()
const isExternalLink = computed(() => isExternal(props.to))

const type = computed(() => {
  if (isExternalLink.value) {
    return 'a'
  }
  return 'router-link'
})

const linkProps = (to) => {
  if (isExternalLink.value) {
    return {
      href: to,
      target: '_blank',
      rel: 'noopener'
    }
  }
  return {
    to: to
  }
}

const handleClick = (event) => {
  if (isExternalLink.value) {
    // 外部链接，让默认行为处理
    return
  }
  
  // 阻止默认行为，手动处理路由导航
  event.preventDefault()
  router.push(props.to)
}
</script>