// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import { store } from './store'
import './../node_modules/bulma/css/bulma.css'
import 'vue-material-design-icons/styles.css'

Vue.config.productionTip = false

const token = localStorage.getItem('user-token')
if (token) {
  axios.defaults.headers.common['Authorization'] = 'Bearer ' + token
}

const mode = 'development'
console.log(process.env.NODE_ENV)
export const host = '138.68.99.124'
export const backPort = '8888'
export const authUrl = '8888'
// export const host = mode === 'production' ? '138.68.99.124' : 'localhost'
// export const backPort = mode === 'production' ? '8888' : '8090'
// export const authUrl = mode === 'production' ? '8888' : '9999'

axios.defaults.baseURL = `http://${host}:${backPort}`

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
