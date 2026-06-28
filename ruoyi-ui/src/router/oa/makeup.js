import Layout from '@/layout'

export default [
  {
    path: '/oa/attendance/makeup/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/attendance/makeupForm'),
        name: 'OaAttendanceMakeupForm',
        meta: { title: '补卡申请', activeMenu: '/oa/attendance/makeup' }
      }
    ]
  }
]
