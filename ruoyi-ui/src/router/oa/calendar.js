/* Layout */
import Layout from '@/layout'

export default {
  path: '/oa/calendar',
  component: Layout,
  hidden: false,
  permissions: ['oa:calendar:list'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/oa/calendar/index'),
      name: 'OaCalendar',
      meta: { title: 'route.calendar', icon: 'date' }
    }
  ]
}
