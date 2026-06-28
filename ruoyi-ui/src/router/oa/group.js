import Layout from '@/layout'

export default [
  {
    path: '/oa/hr/attendance/group/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/attendance/groupForm'),
        name: 'OaAttendanceGroupForm',
        meta: { title: '考勤组', activeMenu: '/oa/hr/attendance/group' }
      }
    ]
  }
]
