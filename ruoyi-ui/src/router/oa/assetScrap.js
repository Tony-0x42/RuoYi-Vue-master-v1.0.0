import Layout from '@/layout'

export default [
  {
    path: '/oa/asset/scrap',
    component: Layout,
    hidden: true,
    permissions: ['oa:asset:edit'],
    children: [
      {
        path: '',
        component: () => import('@/views/oa/asset/scrapForm'),
        name: 'OaAssetScrapForm',
        meta: { title: '资产报废', activeMenu: '/oa/assetDir/asset' }
      }
    ]
  }
]
