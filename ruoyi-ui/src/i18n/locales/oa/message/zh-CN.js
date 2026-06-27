export default {
  oa: {
    message: {
      title: '消息标题',
      type: '消息类型',
      content: '消息内容',
      sender: '发送人',
      priority: '优先级',
      channel: '发送渠道',
      status: '阅读状态',
      read: '已读',
      unread: '未读',
      readTime: '阅读时间',
      sendMessage: '发送消息',
      view: '查看',
      viewMessage: '查看消息',
      markRead: '标记已读',
      markReadSuccess: '标记已读成功',
      recipient: '接收人',
      sendSuccess: '发送成功',
      pleaseSelect: '请选择至少一条记录',
      sendLog: '发送日志',
      retry: '重试',
      todo: '流程待办',
      result: '流程结果',
      notice: '公告',
      system: '系统通知',
      low: '低',
      normal: '普通',
      high: '高',
      site: '站内信',
      email: '邮件',
      sms: '短信',
      im: 'IM',
      template: {
        title: '模板管理',
        code: '模板编码',
        name: '模板名称',
        type: '模板类型',
        channelsJson: '适用渠道',
        contentJson: '渠道内容',
        variables: '变量定义',
        status: '状态',
        enabled: '启用',
        disabled: '停用',
        addTemplate: '新增模板',
        editTemplate: '修改模板',
        required: {
          code: '模板编码不能为空',
          name: '模板名称不能为空'
        }
      },
      placeholder: {
        title: '请输入消息标题',
        type: '请选择消息类型',
        content: '请输入消息内容',
        priority: '请选择优先级',
        channel: '请选择发送渠道',
        recipient: '请选择接收人'
      },
      required: {
        title: '消息标题不能为空',
        content: '消息内容不能为空',
        recipient: '接收人不能为空'
      },
      confirm: {
        deleteMessage: '是否确认删除消息编号为"{ids}"的数据项？',
        deleteTemplate: '是否确认删除模板编号为"{ids}"的数据项？',
        markRead: '是否确认将选中的消息标记为已读？'
      }
    }
  }
}
