import zhCNBase from './zh-CN'
import enUSBase from './en-US'
import elementZhCN from 'element-ui/lib/locale/lang/zh-CN'
import elementEn from 'element-ui/lib/locale/lang/en'

function deepMerge(target, source) {
  if (!source) return target
  const result = { ...target }
  for (const key of Object.keys(source)) {
    const sourceVal = source[key]
    const targetVal = result[key]
    if (sourceVal && typeof sourceVal === 'object' && !Array.isArray(sourceVal)) {
      result[key] = deepMerge(targetVal || {}, sourceVal)
    } else {
      result[key] = sourceVal
    }
  }
  return result
}

const messages = {
  'zh-CN': deepMerge({ ...zhCNBase }, elementZhCN),
  'en-US': deepMerge({ ...enUSBase }, elementEn)
}

const oaLocaleFiles = require.context('./oa', true, /\/(zh-CN|en-US)\.js$/)
oaLocaleFiles.keys().forEach(key => {
  const locale = key.endsWith('zh-CN.js') ? 'zh-CN' : 'en-US'
  const moduleMessages = oaLocaleFiles(key).default || oaLocaleFiles(key)
  messages[locale] = deepMerge(messages[locale], moduleMessages)
})

export default messages
