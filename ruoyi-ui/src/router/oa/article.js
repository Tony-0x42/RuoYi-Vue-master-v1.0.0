import Layout from '@/layout'

export default [
  {
    path: '/oa/knowledge/knowledgebase/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/knowledgebase/articleForm'),
        name: 'OaKnowledgebaseArticleForm',
        meta: { title: '知识词条', activeMenu: '/oa/knowledge/knowledgebase' }
      }
    ]
  }
]
