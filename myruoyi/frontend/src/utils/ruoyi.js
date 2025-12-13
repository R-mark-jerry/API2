/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

/**
 * 获取请求路径参数
 * @param url
 * @returns {Object}
 */
export function getQueryObject(url) {
  url = url == null ? window.location.href : url
  const search = url.substring(url.lastIndexOf('?') + 1)
  const obj = {}
  const reg = /([^?&=]+)=([^?&=]*)/g
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1)
    let val = decodeURIComponent($2)
    val = String(val)
    obj[name] = val
    return rs
  })
  return obj
}

/**
 * 参数处理
 * @param params
 * @returns {string}
 */
export function tansParams(params) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName]
    const part = encodeURIComponent(propName) + '='
    if (value !== null && value !== '' && typeof (value) !== 'undefined') {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== '' && typeof (value[key]) !== 'undefined') {
            const params = propName + '[' + key + ']'
            const subPart = encodeURIComponent(params) + '='
            result += subPart + encodeURIComponent(value[key]) + '&'
          }
        }
      } else {
        result += part + encodeURIComponent(value) + '&'
      }
    }
  }
  return result
}

/**
 * 获取标准路径
 * @param path
 * @param query
 * @returns {string}
 */
export function getNormalPath(path, query = {}) {
  if (path.indexOf('http') === 0) {
    return path
  }
  let queryStr = ''
  for (const key in query) {
    if (query[key] !== undefined && query[key] !== null && query[key] !== '') {
      queryStr += key + '=' + query[key] + '&'
    }
  }
  if (queryStr !== '') {
    queryStr = '?' + queryStr.substring(0, queryStr.length - 1)
  }
  return path + queryStr
}

/**
 * 获取URL参数
 * @param name
 * @returns {string|null}
 */
export function getQueryString(name) {
  const reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i')
  const r = window.location.search.substr(1).match(reg)
  if (r != null) {
    return decodeURIComponent(r[2])
  }
  return null
}

/**
 * 格式化时间
 * @param time
 * @param pattern
 * @returns {string}
 */
export function parseTime(time, pattern) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
      time = parseInt(time)
    } else if (typeof time === 'string') {
      time = time.replace(new RegExp(/-/gm), '/')
    }
    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

/**
 * 表单重置
 * @param refName
 */
export function resetForm(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].resetFields()
  }
}

/**
 * 添加日期范围
 * @param params
 * @param dateRange
 * @param propName
 */
export function addDateRange(params = {}, dateRange = [], propName = 'range') {
  const search = params
  search.params = {}
  if (dateRange && dateRange.length === 2) {
    if (propName === 'range') {
      search.params.beginTime = dateRange[0]
      search.params.endTime = dateRange[1]
    } else {
      search.params[propName + 'BeginTime'] = dateRange[0]
      search.params[propName + 'EndTime'] = dateRange[1]
    }
  }
  return search
}

/**
 * 回显数据字典
 * @param datas
 * @param value
 * @returns {string}
 */
export function selectDictLabel(datas, value) {
  if (datas === undefined || datas === null || datas.length === 0) {
    return ''
  }
  const actions = datas.find(item => item.dictValue === value)
  if (actions) {
    return actions.dictLabel
  }
  return ''
}

/**
 * 回显数据字典（字符串数组）
 * @param datas
 * @param value
 * @returns {string}
 */
export function selectDictLabels(datas, value, separator = ',') {
  if (datas === undefined || datas === null || datas.length === 0) {
    return ''
  }
  const actions = datas.filter(item => value.split(separator).includes(item.dictValue))
  return actions.map(item => item.dictLabel).join(separator)
}

/**
 * 转换字符串，undefined,null等转化为""
 * @param str
 * @returns {string}
 */
export function parseStrEmpty(str) {
  if (str === undefined || str === null || str === '') {
    return ''
  }
  return str
}

/**
 * 数据合并
 * @param source
 * @param target
 * @returns {Object}
 */
export function mergeObject(source, target) {
  if (typeof target !== 'object') {
    source = target
    return source
  }
  for (const key in target) {
    if (target.hasOwnProperty(key)) {
      const sourceProp = source[key]
      if (sourceProp && typeof sourceProp === 'object') {
        mergeObject(sourceProp, target[key])
      } else {
        source[key] = target[key]
      }
    }
  }
  return source
}

/**
 * 构造树型结构数据
 * @param data
 * @param id
 * @param parentId
 * @returns {Array}
 */
export function handleTree(data, id, parentId) {
  const config = {
    id: id || 'id',
    parentId: parentId || 'parentId'
  }

  const childrenListMap = {}
  const nodeIds = {}
  const tree = []

  for (const d of data) {
    const parentId = d[config.parentId]
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = []
    }
    nodeIds[d[config.id]] = d
    childrenListMap[parentId].push(d)
  }

  for (const d of data) {
    const parentId = d[config.parentId]
    if (nodeIds[parentId] == null) {
      tree.push(d)
    }
  }

  for (const t of tree) {
    adaptToChildrenList(t)
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o.children = childrenListMap[o[config.id]]
    }
    if (o.children) {
      for (const c of o.children) {
        adaptToChildrenList(c)
      }
    }
  }
  return tree
}