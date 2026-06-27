export default {
  route: {
    task: '任务协作'
  },
  oa: {
    task: {
      title: '任务标题',
      description: '任务描述',
      owner: '负责人',
      participant: '参与人',
      priorityLabel: '优先级',
      statusLabel: '状态',
      progress: '进度',
      startTime: '开始时间',
      endTime: '截止时间',
      subtask: '子任务',
      comment: '评论',
      board: '看板',
      list: '列表',
      statistics: '统计',
      detail: '任务详情',
      addTask: '新增任务',
      editTask: '修改任务',
      addSubtask: '添加子任务',
      view: '查看',
      status: {
        pending: '待处理',
        inProgress: '进行中',
        completed: '已完成',
        cancelled: '已取消'
      },
      priority: {
        urgent: '紧急',
        high: '高',
        medium: '中',
        low: '低'
      },
      placeholder: {
        title: '请输入任务标题',
        description: '请输入任务描述',
        owner: '请选择负责人',
        participant: '请选择参与人',
        priority: '请选择优先级',
        status: '请选择状态',
        timeRange: '请选择起止时间',
        subtaskTitle: '请输入子任务标题'
      },
      required: {
        title: '任务标题不能为空'
      },
      confirm: {
        deleteTask: '是否确认删除任务编号为"{ids}"的数据项？'
      }
    }
  }
}
