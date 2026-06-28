import Layout from '@/layout'

export default [
  {
    path: '/oa/expenseDir/expenseInvoice/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/expense/invoiceForm'),
        name: 'OaExpenseInvoiceForm',
        meta: { title: '发票', activeMenu: '/oa/expenseDir/expenseInvoice' }
      }
    ]
  }
]
