import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import qs from 'qs'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    token: localStorage.getItem('user-token') || '',
    status: ''
  },
  getters: {
    isAuthenticated: state => !!state.token,
    authStatus: state => state.status
  },
  mutations: {
    AUTH_REQUEST: state => {
      state.status = 'loading'
    },
    AUTH_SUCCESS: (state, token) => {
      state.status = 'success'
      state.token = token
    },
    AUTH_ERROR: (state) => {
      state.status = 'error'
    }
  },
  actions: {
    AUTHENTICATE: (context, user) => {
      return new Promise((resolve, reject) => {
        context.commit('AUTH_REQUEST')
        axios.post('http://localhost:9999/auth/login', qs.stringify(user), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
          .then(response => {
            context.commit('AUTH_SUCCESS', response.data.accessToken)
            resolve()
          }).catch(e => {
            context.commit('AUTH_ERROR')
            reject(e)
          })
      })
    }
  }
})
