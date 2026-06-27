import Layout from '@/layout'

export default [
  {
    path: '/oa/task/detail',
    component: Layout,
    hidden: true,
    permissions: ['oa:task:list'],
    children: [
      {
        path: ':id',
        component: () => import('@/views/oa/task/index'),
        name: 'OaTaskDetail',
        meta: { title: 'oa.task.detail', activeMenu: '/oa/task' }
      }
    ]
  }
]
