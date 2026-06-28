import Layout from '@/layout'

export default [
  {
    path: '/oa/meeting/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/meeting/meetingForm'),
        name: 'OaMeetingForm',
        meta: { title: '会议', activeMenu: '/oa/meeting' }
      }
    ]
  }
]
