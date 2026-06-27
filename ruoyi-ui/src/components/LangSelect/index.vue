<template>
  <el-dropdown trigger="click" class="international" @command="handleSetLanguage">
    <div>
      <svg-icon icon-class="language" />
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="zh-CN" :disabled="language === 'zh-CN'">
        中文
      </el-dropdown-item>
      <el-dropdown-item command="en-US" :disabled="language === 'en-US'">
        English
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
export default {
  computed: {
    language() {
      return this.$store.getters.locale
    }
  },
  methods: {
    handleSetLanguage(lang) {
      const isLoggedIn = !!this.$store.getters.token
      if (isLoggedIn) {
        this.$store.dispatch('UpdateLang', lang).then(() => {
          location.reload()
        })
      } else {
        this.$i18n.locale = lang
        this.$store.dispatch('app/setLocale', lang).then(() => {
          location.reload()
        })
      }
    }
  }
}
</script>
