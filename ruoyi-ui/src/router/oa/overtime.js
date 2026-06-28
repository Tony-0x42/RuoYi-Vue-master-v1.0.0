import Layout from '@/layout'

export default [
  {
    path: '/oa/hr/overtime/form',
    component: Layout,
    hidden: true,
    permissions: ['oa:attendanceOvertime:add'],
    children: [
      {
        path: '',
        component: () => import('@/views/oa/attendance/overtimeForm'),
        name: 'OaAttendanceOvertimeForm',
        meta: { title: '加班申请', activeMenu: '/oa/hr/overtime' }
      }
    ]
  }
]
