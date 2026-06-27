export default {
  route: {
    meeting: '会议管理',
    meetingRoom: '会议室管理'
  },
  oa: {
    meeting: {
      title: '会议管理',
      roomTitle: '会议室管理',
      search: {
        title: '会议主题',
        titlePlaceholder: '请输入会议主题',
        room: '会议室',
        status: '会议状态',
        dateRange: '时间范围'
      },
      status: {
        pending: '待开始',
        ongoing: '进行中',
        finished: '已结束',
        cancelled: '已取消'
      },
      room: {
        code: '会议室编码',
        name: '会议室名称',
        location: '位置',
        capacity: '容量',
        devices: '设备',
        status: '状态',
        picUrl: '图片',
        codePlaceholder: '请输入会议室编码',
        namePlaceholder: '请输入会议室名称',
        locationPlaceholder: '请输入位置',
        devicesPlaceholder: '请输入设备说明',
        enable: '启用',
        disable: '停用'
      },
      form: {
        title: '会议主题',
        titleRequired: '会议主题不能为空',
        timeRange: '会议时间',
        timeRequired: '请选择会议时间',
        roomId: '会议室',
        roomIdRequired: '请选择会议室',
        content: '会议内容',
        attendees: '参与人',
        organizer: '组织者',
        remark: '备注'
      },
      add: '新增会议',
      edit: '修改会议',
      detail: '会议详情',
      cancel: '取消会议',
      cancelSuccess: '取消成功',
      signIn: '签到',
      signInSuccess: '签到成功',
      minutes: '会议纪要',
      saveMinutes: '保存纪要',
      minutesSuccess: '纪要保存成功',
      occupancy: '占用视图',
      deleteConfirm: '是否确认删除会议编号为"{ids}"的数据项？',
      deleteRoomConfirm: '是否确认删除会议室编号为"{ids}"的数据项？',
      required: {
        code: '会议室编码不能为空',
        name: '会议室名称不能为空',
        capacity: '容量不能为空'
      }
    }
  }
}
