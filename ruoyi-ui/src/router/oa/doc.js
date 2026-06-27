import Layout from '@/layout'

export default [
  {
    path: '/oa/doc/recycle',
    component: Layout,
    hidden: true,
    permissions: ['oa:doc:list'],
    children: [
      {
        path: '',
        component: () => import('@/views/oa/doc/index'),
        name: 'OaDocRecycle',
        meta: { title: 'oa.doc.recycle', activeMenu: '/oa/doc' }
      }
    ]
  }
]
