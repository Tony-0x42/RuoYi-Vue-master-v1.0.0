import Layout from '@/layout'

export default [
  {
    path: '/oa/calendar/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/calendar/eventForm'),
        name: 'OaCalendarEventForm',
        meta: { title: '日程', activeMenu: '/oa/calendar' }
      }
    ]
  }
]
