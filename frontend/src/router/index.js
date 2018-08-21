import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/auth/KanbanLogin'
import KanbanBoard from '@/components/KanbanBoard'

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
    }
  ]
})
