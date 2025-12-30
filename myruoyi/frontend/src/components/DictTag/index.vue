<template>
  <el-tag
    :type="tagType"
    :effect="effect"
    :size="size"
    :class="tagClass"
  >
    {{ label }}
  </el-tag>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  options: {
    type: Array,
    required: true
  },
  value: {
    type: [String, Number],
    required: true
  },
  type: {
    type: String,
    default: ''
  },
  effect: {
    type: String,
    default: 'light'
  },
  size: {
    type: String,
    default: 'default'
  },
  className: {
    type: String,
    default: ''
  }
})

const label = computed(() => {
  const option = props.options.find(item => item.value == props.value)
  return option ? option.label : props.value
})

const tagType = computed(() => {
  if (props.type) {
    return props.type
  }
  // 根据值自动判断类型
  const option = props.options.find(item => item.value == props.value)
  return option ? option.type || 'primary' : 'primary'
})

const tagClass = computed(() => {
  return props.className
})
</script>