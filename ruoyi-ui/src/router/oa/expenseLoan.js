import Layout from '@/layout'

export default [
  {
    path: '/oa/expenseDir/expenseLoan/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/expense/loanForm'),
        name: 'OaExpenseLoanForm',
        meta: { title: '借款单', activeMenu: '/oa/expenseDir/expenseLoan' }
      }
    ]
  }
]
