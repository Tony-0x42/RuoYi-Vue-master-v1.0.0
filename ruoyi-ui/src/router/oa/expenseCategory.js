import Layout from '@/layout'

export default [
  {
    path: '/oa/expenseDir/expenseCategory/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/expense/categoryForm'),
        name: 'OaExpenseCategoryForm',
        meta: { title: '费用类型', activeMenu: '/oa/expenseDir/expenseCategory' }
      }
    ]
  }
]
