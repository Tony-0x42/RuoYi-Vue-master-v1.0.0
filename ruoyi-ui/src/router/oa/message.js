import Layout from '@/layout'

export default [
  {
    path: '/oa/message/form',
    component: Layout,
    hidden: true,
    permissions: ['oa:message:send'],
    children: [
      {
        path: '',
        component: () => import('@/views/oa/message/messageForm'),
        name: 'OaMessageForm',
        meta: { title: 'oa.message.sendMessage', activeMenu: '/oa/message' }
      }
    ]
  }
]
