import store from '@/store'
import defaultSettings from '@/settings'
import i18n from '@/i18n'

/**
 * 动态修改标题
 */
export function useDynamicTitle() {
  if (store.state.settings.dynamicTitle) {
    const pageTitle = i18n.t(store.state.settings.title)
    document.title = pageTitle + ' - ' + defaultSettings.title
  } else {
    document.title = defaultSettings.title
  }
}