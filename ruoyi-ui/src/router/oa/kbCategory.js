import Layout from '@/layout'

export default [
  {
    path: '/oa/knowledge/knowledgebaseCategory/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/knowledgebase/kbCategoryForm'),
        name: 'OaKnowledgebaseCategoryForm',
        meta: { title: '知识分类', activeMenu: '/oa/knowledge/knowledgebaseCategory' }
      }
    ]
  }
]
