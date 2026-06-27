<template>
  <div class="app-container">
    <h3 class="page-title">{{ pageTitle }}</h3>
    <slot name="form" />
    <oa-form-actions :mode="mode" @submit="handleSubmit" @save="handleSave" @close="handleClose" />
  </div>
</template>

<script>
import OaFormActions from "./OaFormActions"

export default {
  name: "OaFormPage",
  components: {
    OaFormActions
  },
  props: {
    title: {
      type: String,
      default: undefined
    }
  },
  computed: {
    mode() {
      const m = this.$route.query.mode
      return ["add", "edit", "detail"].includes(m) ? m : "add"
    },
    formId() {
      return this.$route.query.id
    },
    pageTitle() {
      const base = this.title || (this.$route.meta && this.$route.meta.title) || ""
      const prefixMap = {
        add: "新增",
        edit: "编辑",
        detail: "查看"
      }
      const prefix = prefixMap[this.mode] || ""
      return prefix + base
    }
  },
  methods: {
    getFormComponent() {
      const slot = this.$slots.form
      if (!slot || !slot.length) {
        return null
      }
      return slot[0].componentInstance || null
    },
    handleSubmit() {
      const form = this.getFormComponent()
      if (form && typeof form.validate === "function") {
        form.validate(valid => {
          if (valid) {
            this.$emit("submit")
          }
        })
      } else {
        this.$emit("submit")
      }
    },
    handleSave() {
      this.$emit("save")
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
