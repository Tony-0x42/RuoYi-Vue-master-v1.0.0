import Layout from '@/layout'

export default [
  {
    path: '/oa/assetDir/asset/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/asset/assetForm'),
        name: 'OaAssetForm',
        meta: { title: '资产台账', activeMenu: '/oa/assetDir/asset' }
      }
    ]
  }
]
