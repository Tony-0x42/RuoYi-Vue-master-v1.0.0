import Layout from '@/layout'

export default [
  {
    path: '/oa/expenseBudget/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/expense/budgetForm'),
        name: 'OaExpenseBudgetForm',
        meta: { title: '预算', activeMenu: '/oa/expenseBudget' }
      }
    ]
  }
]
