import Layout from '@/layout'

export default [
  {
    path: '/oa/hr/trip/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/attendance/tripForm'),
        name: 'OaAttendanceTripForm',
        meta: { title: '出差申请', activeMenu: '/oa/hr/trip' }
      }
    ]
  }
]
