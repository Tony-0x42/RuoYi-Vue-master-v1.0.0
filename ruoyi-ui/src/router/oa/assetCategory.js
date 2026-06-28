import Layout from '@/layout'

export default [
  {
    path: '/oa/assetDir/assetCategory/form',
    component: Layout,
    hidden: true,
    permissions: ['oa:assetCategory:add'],
    children: [
      {
        path: '',
        component: () => import('@/views/oa/asset/categoryForm'),
        name: 'OaAssetCategoryForm',
        meta: { title: '资产分类', activeMenu: '/oa/assetDir/assetCategory' }
      }
    ]
  }
]
