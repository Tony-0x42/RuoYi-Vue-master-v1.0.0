import Layout from '@/layout'

export default [
  {
    path: '/oa/noticeCategory/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/notice/noticeCategoryForm'),
        name: 'OaNoticeCategoryForm',
        meta: { title: '公告分类', activeMenu: '/oa/noticeCategory' }
      }
    ]
  }
]
