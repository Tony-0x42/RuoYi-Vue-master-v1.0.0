<script>
import i18n from '@/i18n'

export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    },
    name: {
      type: String,
      default: ''
    }
  },
  render(h, context) {
    const { icon, title, name } = context.props
    const vnodes = []
    const parent = context.parent

    let displayTitle = title
    if (name && parent && parent.$te) {
      const key = `menu.${name}`
      if (parent.$te(key)) {
        displayTitle = parent.$t(key)
      }
    }

    // Translate title key or fallback string
    if (displayTitle) {
      displayTitle = (parent && parent.$t) ? parent.$t(displayTitle) : i18n.t(displayTitle)
    }

    if (icon) {
      vnodes.push(<svg-icon icon-class={icon}/>)
    }

    if (displayTitle) {
      if (displayTitle.length > 5) {
        vnodes.push(<span slot='title' title={(displayTitle)}>{(displayTitle)}</span>)
      } else {
        vnodes.push(<span slot='title'>{(displayTitle)}</span>)
      }
    }
    return vnodes
  }
}
</script>
