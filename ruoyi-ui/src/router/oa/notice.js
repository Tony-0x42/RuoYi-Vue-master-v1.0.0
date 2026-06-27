import Layout from '@/layout'

export default [
  {
    path: '/oa/notice/detail',
    component: Layout,
    hidden: true,
    permissions: ['oa:notice:list'],
    children: [
      {
        path: ':id',
        component: () => import('@/views/oa/notice/detail'),
        name: 'OaNoticeDetail',
        meta: { title: 'oa.notice.detail', activeMenu: '/oa/notice' }
      }
    ]
  }
]
