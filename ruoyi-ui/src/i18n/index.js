import Vue from 'vue'
import VueI18n from 'vue-i18n'
import messages from './locales'

Vue.use(VueI18n)

export const FALLBACK_LOCALE = 'zh-CN'
export const DEFAULT_LOCALE = 'zh-CN'

const savedLocale = localStorage.getItem('ruoyi-locale') || DEFAULT_LOCALE

const i18n = new VueI18n({
  locale: savedLocale,
  fallbackLocale: FALLBACK_LOCALE,
  silentFallbackWarn: true,
  silentTranslationWarn: true,
  messages
})

export function setLocale(lang) {
  if (!messages[lang]) {
    console.warn(`[i18n] locale ${lang} not found, fallback to ${FALLBACK_LOCALE}`)
    lang = FALLBACK_LOCALE
  }
  i18n.locale = lang
  localStorage.setItem('ruoyi-locale', lang)
  document.querySelector('html').setAttribute('lang', lang)
}

export default i18n
