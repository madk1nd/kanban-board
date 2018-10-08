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

axios.defaults.baseURL = ''

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
