<template>
  <div class="welcome-card">
    <div class="welcome-info">
      <div class="welcome-title">您好，{{ displayName }}，欢迎进入工作台</div>
      <div class="welcome-desc">{{ welcomeText }} · 今天是 {{ currentDate }}</div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'WelcomeCard',
  data() {
    return {
      currentDate: '',
      welcomeText: ''
    }
  },
  computed: {
    ...mapGetters(['name', 'nickName']),
    displayName() {
      return this.nickName || this.name || ''
    }
  },
  created() {
    this.setDate()
  },
  methods: {
    setDate() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
      this.currentDate = `${year}年${month}月${day}日 ${weekDays[now.getDay()]}`
      const hour = now.getHours()
      if (hour < 12) this.welcomeText = '上午好'
      else if (hour < 18) this.welcomeText = '下午好'
      else this.welcomeText = '晚上好'
    }
  }
}
</script>

<style scoped lang="scss">
.welcome-card {
  background: linear-gradient(90deg, #4a90e2 0%, #63b3ed 100%);
  border-radius: 8px;
  padding: 24px 28px;
  color: #fff;
}
.welcome-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
}
.welcome-desc {
  font-size: 14px;
  opacity: 0.9;
}
</style>
