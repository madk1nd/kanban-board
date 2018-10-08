import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/auth/KanbanLogin'
import KanbanBoard from '@/components/KanbanBoard'
import KanbanRegistration from '@/components/auth/KanbanRegistration'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/board',
      name: 'KanbanBoard',
      component: KanbanBoard
    },
    {
      path: '/register',
      name: 'KanbanRegistration',
      component: KanbanRegistration
    }
  ]
})
