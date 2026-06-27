export default {
  route: {
    calendar: '日程管理'
  },
  calendar: {
    title: '日程管理',
    search: {
      title: '日程标题',
      titlePlaceholder: '请输入日程标题',
      type: '日程类型',
      dateRange: '时间范围'
    },
    type: {
      personal: '个人',
      meeting: '会议',
      leave: '请假',
      trip: '出差'
    },
    allDay: '全天',
    startTime: '开始时间',
    endTime: '结束时间',
    location: '地点',
    attendees: '参与人',
    source: '来源',
    status: '状态',
    add: '新增日程',
    edit: '修改日程',
    detail: '日程详情',
    deleteConfirm: '是否确认删除日程编号为"{eventIds}"的数据项？',
    form: {
      title: '日程标题',
      titleRequired: '日程标题不能为空',
      typeRequired: '请选择日程类型',
      timeRequired: '请选择起止时间',
      locationPlaceholder: '请输入地点',
      remark: '备注'
    }
  }
}
