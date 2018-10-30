import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/auth/KanbanLogin'
import KanbanBoard from '@/components/KanbanBoard'
import KanbanRegistration from '@/components/auth/KanbanRegistration'
import KanbanConfirmation from '@/components/auth/KanbanConfirmation'
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
