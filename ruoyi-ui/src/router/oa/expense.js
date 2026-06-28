import Layout from '@/layout'

export default [
  {
    path: '/oa/expense/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/expense/form'),
        name: 'OaExpenseForm',
        meta: { title: '报销单', activeMenu: '/oa/expense' }
      }
    ]
  }
]
