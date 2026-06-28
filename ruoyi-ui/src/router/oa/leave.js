import Layout from '@/layout'

export default [
  {
    path: '/oa/hr/leave/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/attendance/leaveForm'),
        name: 'OaAttendanceLeaveForm',
        meta: { title: '请假申请', activeMenu: '/oa/hr/leave' }
      }
    ]
  }
]
