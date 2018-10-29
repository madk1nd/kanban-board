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

// const mode = 'development'
console.log(`host = ${process.env.HOST}`)
console.log(`back port = ${process.env.BACK_PORT}`)
console.log(`auth port = ${process.env.AUTH_PORT}`)
export const host = process.env.HOST
export const backPort = process.env.BACK_PORT
export const authUrl = process.env.AUTH_PORT

axios.defaults.baseURL = `http://${host}:${backPort}`

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
