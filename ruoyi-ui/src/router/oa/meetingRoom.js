import Layout from '@/layout'

export default [
  {
    path: '/oa/meetingDir/meetingRoom/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/meeting/meetingRoomForm'),
        name: 'OaMeetingRoomForm',
        meta: { title: '会议室', activeMenu: '/oa/meetingDir/meetingRoom' }
      }
    ]
  }
]
