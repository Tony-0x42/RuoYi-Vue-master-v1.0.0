import Layout from '@/layout'

export default [
  {
    path: '/oa/assetDir/assetInventory/form',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/oa/asset/inventoryForm'),
        name: 'OaAssetInventoryForm',
        meta: { title: '盘点任务', activeMenu: '/oa/assetDir/assetInventory' }
      }
    ]
  }
]
