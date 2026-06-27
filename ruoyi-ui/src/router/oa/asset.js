import Layout from '@/layout'

export default [
  {
    path: '/oa/asset/detail',
    component: Layout,
    hidden: true,
    permissions: ['oa:asset:list'],
    children: [
      {
        path: ':id',
        component: () => import('@/views/oa/asset/detail'),
        name: 'OaAssetDetail',
        meta: { title: 'oa.asset.detail', activeMenu: '/oa/asset' }
      }
    ]
  }
]
