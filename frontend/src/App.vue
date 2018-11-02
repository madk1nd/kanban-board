<template>
  <div id="app">
    <kanban-header></kanban-header>
    <router-view/>
    <kanban-footer></kanban-footer>
  </div>
</template>

<script>
import KanbanHeader from '@/pages/KanbanHeader'
import KanbanFooter from '@/pages/KanbanFooter'
import axios from 'axios'
import moment from 'moment'
import {host, authUrl} from './main'

export default {
  name: 'App',
  components: {
    'kanban-header': KanbanHeader,
    'kanban-footer': KanbanFooter
  },
  created () {
    axios.interceptors.request.use(config => {
      return new Promise((resolve, reject) => {
        let time = localStorage.getItem('expired-in')
        if (time && moment(time, 'x') < moment().utc()) {
          let refresh = localStorage.getItem('refresh-token')
          fetch(`http://${host}:${authUrl}/auth/refresh`, {
            method: 'POST',
            body: refresh
          })
            .then(resp => resp.json())
            .then(json => {
              let accessToken = json.accessToken
              let auth = 'Bearer ' + accessToken
              axios.defaults.headers.common['Authorization'] = auth
              this.$store.commit('AUTH_SUCCESS', accessToken)
              localStorage.setItem('user-token', accessToken)
              localStorage.setItem('refresh-token', json.refreshToken)
              localStorage.setItem('expired-in', json.accessTokenExpiredIn)
              config.headers.Authorization = auth
              resolve(config)
            })
            .catch(e => resolve(config))
        } else {
          resolve(config)
        }
      })
    })
    axios.interceptors.response.use(
      undefined,
      err => {
        return new Promise((resolve, reject) => {
          if (err.response.status === 401 && err.response.config && !err.response.config.__isRetryRequest) {
            this.$store.dispatch('AUTH_LOGOUT')
            this.$router.push('/')
          }
          throw err
        })
      })
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
.radius-top {
  -webkit-border-radius: 5px 5px 0 0;
  -moz-border-radius: 5px 5px 0 0;
  border-radius: 5px 5px 0 0;
}
.width-full {
  width: 100%;
}
</style>
