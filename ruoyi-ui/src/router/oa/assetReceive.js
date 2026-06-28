import Layout from '@/layout'

export default [
  {
    path: '/oa/asset/receive',
    component: Layout,
    hidden: true,
    permissions: ['oa:asset:edit'],
    children: [
      {
        path: '',
        component: () => import('@/views/oa/asset/receiveForm'),
        name: 'OaAssetReceiveForm',
        meta: { title: '资产领用', activeMenu: '/oa/assetDir/asset' }
      }
    ]
  }
]
