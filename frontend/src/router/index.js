import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/pages/auth/KanbanLogin'
import KanbanBoard from '@/pages/KanbanBoard'
import KanbanRegistration from '@/pages/auth/KanbanRegistration'
import KanbanConfirmation from '@/pages/auth/KanbanConfirmation'
import {store} from '@/store'

Vue.use(Router)

const ifAuthenticated = (to, from, next) => {
  if (store.getters.isAuthenticated) {
    next()
    return
  }
  next('/')
}

const ifNotAuthenticated = (to, from, next) => {
  if (!store.getters.isAuthenticated) {
    next()
    return
  }
  next('/board')
}

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login,
      beforeEnter: ifNotAuthenticated
    },
    {
      path: '/board',
      name: 'KanbanBoard',
      component: KanbanBoard,
      beforeEnter: ifAuthenticated
    },
    {
      path: '/register',
      name: 'KanbanRegistration',
      component: KanbanRegistration
    },
    {
      path: '/confirm',
      name: 'KanbanConfirmation',
      component: KanbanConfirmation,
      props: true
    }
  ]
})
