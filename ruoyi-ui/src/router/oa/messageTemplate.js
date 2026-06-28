import Layout from '@/layout'

export default [
  {
    path: '/oa/messageTemplate/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/message/messageTemplateForm'),
        name: 'OaMessageTemplateForm',
        meta: { title: 'oa.message.template.title', activeMenu: '/oa/messageTemplate' }
      }
    ]
  }
]
