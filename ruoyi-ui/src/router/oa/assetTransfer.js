import Layout from '@/layout'

export default [
  {
    path: '/oa/asset/transfer',
    component: Layout,
    hidden: true,
    permissions: ['oa:asset:edit'],
    children: [
      {
        path: '',
        component: () => import('@/views/oa/asset/transferForm'),
        name: 'OaAssetTransferForm',
        meta: { title: '资产调拨', activeMenu: '/oa/assetDir/asset' }
      }
    ]
  }
]
