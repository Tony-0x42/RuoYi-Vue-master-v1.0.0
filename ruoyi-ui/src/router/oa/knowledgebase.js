import Layout from '@/layout'

export default [
  {
    path: '/oa/knowledgebase/detail',
    component: Layout,
    hidden: true,
    permissions: ['oa:knowledgebase:list'],
    children: [
      {
        path: ':id',
        component: () => import('@/views/oa/knowledgebase/detail'),
        name: 'OaKnowledgebaseDetail',
        meta: { title: 'oa.knowledgebase.detail', activeMenu: '/oa/knowledgebase' }
      }
    ]
  }
]
