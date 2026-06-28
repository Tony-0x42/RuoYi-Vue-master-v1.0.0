import Layout from '@/layout'

export default [
  {
    path: '/oa/expenseStandard/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/expense/standardForm'),
        name: 'OaExpenseStandardForm',
        meta: { title: '费用标准', activeMenu: '/oa/expenseStandard' }
      }
    ]
  }
]
