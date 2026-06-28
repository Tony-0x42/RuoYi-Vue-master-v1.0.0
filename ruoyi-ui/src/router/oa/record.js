import Layout from '@/layout'

export default [
  {
    path: '/oa/attendance/record/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/attendance/recordForm'),
        name: 'OaAttendanceRecordForm',
        meta: { title: '打卡记录', activeMenu: '/oa/attendance/record' }
      }
    ]
  }
]
