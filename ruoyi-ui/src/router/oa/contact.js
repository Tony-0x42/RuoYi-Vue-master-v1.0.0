/* Layout */
import Layout from '@/layout'

export default [
  {
    path: '/oa/addressbook/form',
    component: Layout,
    hidden: true,
    permissions: ['oa:addressbook:list'],
    children: [
      {
        path: '',
        component: () => import('@/views/oa/addressbook/contactForm'),
        name: 'OaAddressBookContactForm',
        meta: { title: '添加常用联系人', activeMenu: '/oa/addressbook' }
      }
    ]
  }
]
