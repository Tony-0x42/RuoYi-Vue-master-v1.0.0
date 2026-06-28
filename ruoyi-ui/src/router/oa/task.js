import Layout from '@/layout'

export default [
  {
    path: '/oa/schedule/task/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/task/taskForm'),
        name: 'OaTaskForm',
        meta: { title: '任务', activeMenu: '/oa/schedule/task' }
      }
    ]
  }
]
