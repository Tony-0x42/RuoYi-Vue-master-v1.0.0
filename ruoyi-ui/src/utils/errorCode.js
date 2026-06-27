import i18n from '@/i18n'

const codeMap = {
  '401': 'error.code.401',
  '403': 'error.code.403',
  '404': 'error.code.404',
  'default': 'error.code.default'
}

export default function getErrorMsg(code) {
  const key = codeMap[code] || codeMap['default']
  return i18n.t(key)
}
