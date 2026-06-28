import Layout from '@/layout'

export default [
  {
    path: '/oa/asset/repair',
    component: Layout,
    hidden: true,
    permissions: ['oa:asset:edit'],
    children: [
      {
        path: '',
        component: () => import('@/views/oa/asset/repairForm'),
        name: 'OaAssetRepairForm',
        meta: { title: '资产维修', activeMenu: '/oa/assetDir/asset' }
      }
    ]
  }
]
