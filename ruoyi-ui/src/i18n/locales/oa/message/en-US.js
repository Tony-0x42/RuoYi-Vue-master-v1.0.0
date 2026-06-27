export default {
  oa: {
    message: {
      title: 'Message Title',
      type: 'Message Type',
      content: 'Message Content',
      sender: 'Sender',
      priority: 'Priority',
      channel: 'Channel',
      status: 'Read Status',
      read: 'Read',
      unread: 'Unread',
      readTime: 'Read Time',
      sendMessage: 'Send Message',
      view: 'View',
      viewMessage: 'View Message',
      markRead: 'Mark as Read',
      markReadSuccess: 'Marked as read successfully',
      recipient: 'Recipients',
      sendSuccess: 'Sent successfully',
      pleaseSelect: 'Please select at least one record',
      sendLog: 'Send Log',
      retry: 'Retry',
      todo: 'Todo',
      result: 'Result',
      notice: 'Notice',
      system: 'System',
      low: 'Low',
      normal: 'Normal',
      high: 'High',
      site: 'Site',
      email: 'Email',
      sms: 'SMS',
      im: 'IM',
      template: {
        title: 'Template Management',
        code: 'Template Code',
        name: 'Template Name',
        type: 'Template Type',
        channelsJson: 'Channels',
        contentJson: 'Channel Content',
        variables: 'Variables',
        status: 'Status',
        enabled: 'Enabled',
        disabled: 'Disabled',
        addTemplate: 'Add Template',
        editTemplate: 'Edit Template',
        required: {
          code: 'Template code is required',
          name: 'Template name is required'
        }
      },
      placeholder: {
        title: 'Please enter message title',
        type: 'Please select message type',
        content: 'Please enter message content',
        priority: 'Please select priority',
        channel: 'Please select channel',
        recipient: 'Please select recipients'
      },
      required: {
        title: 'Message title is required',
        content: 'Message content is required',
        recipient: 'Recipients are required'
      },
      confirm: {
        deleteMessage: 'Are you sure to delete message(s) "{ids}"?',
        deleteTemplate: 'Are you sure to delete template(s) "{ids}"?',
        markRead: 'Are you sure to mark selected messages as read?'
      }
    }
  }
}
