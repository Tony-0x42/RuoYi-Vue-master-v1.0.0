import Layout from '@/layout'

export default [
  {
    path: '/oa/attendance/leave/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/attendance/leaveForm'),
        name: 'OaAttendanceLeaveForm',
        meta: { title: '请假申请', activeMenu: '/oa/attendance/leave' }
      }
    ]
  }
]
