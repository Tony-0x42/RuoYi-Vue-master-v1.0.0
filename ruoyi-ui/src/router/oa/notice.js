import Layout from '@/layout'

export default [
  {
    path: '/oa/comm/notice/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/notice/noticeForm'),
        name: 'OaNoticeForm',
        meta: { title: '公告', activeMenu: '/oa/comm/notice' }
      }
    ]
  }
]
