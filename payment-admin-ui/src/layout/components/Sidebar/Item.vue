<script>
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
    }
  },
  render(h, context) {
    const { icon, title } = context.props
    const vnodes = []

    if (icon) {
      vnodes.push(<svg-icon icon-class={icon} />)
    }

    if (title) {
      // 检查是否为翻译key（包含点号的字符串）
      const displayTitle = title.includes('.') ? context.parent.$t(title) : title
      vnodes.push(<span slot='title'>{displayTitle}</span>)
    }
    return vnodes
  }
}
</script>
