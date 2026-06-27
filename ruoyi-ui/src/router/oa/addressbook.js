/* Layout */
import Layout from '@/layout'

export default {
  path: '/oa/addressbook',
  component: Layout,
  hidden: false,
  permissions: ['oa:addressbook:list'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/oa/addressbook/index'),
      name: 'OaAddressBook',
      meta: { title: 'route.addressBook', icon: 'tree' }
    }
  ]
}
