import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import 'es6-promise/auto'
import qs from 'qs'
import moment from 'moment'

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
    },
    AUTH_LOGOUT: (state) => {
      state.token = ''
    },
    CLEAR_STORAGE: () => {
      localStorage.removeItem('user-token')
      localStorage.removeItem('refresh-token')
      localStorage.removeItem('expired-in')
    }
  },
  actions: {
    AUTHENTICATE: ({commit}, user) => {
      return new Promise((resolve, reject) => {
        console.log(localStorage.getItem('expired-in'))
        commit('AUTH_REQUEST')
        axios.post('http://auth:9999/auth/login', qs.stringify(user), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
          .then(response => {
            let accessToken = response.data.accessToken
            axios.defaults.headers.common['Authorization'] = 'Bearer ' + accessToken
            commit('AUTH_SUCCESS', accessToken)
            localStorage.setItem('user-token', accessToken) // store the token in localstorage
            localStorage.setItem('refresh-token', response.data.refreshToken)
            localStorage.setItem('expired-in', response.data.accessTokenExpiredIn)
            resolve()
          }).catch(e => {
            commit('CLEAR_STORAGE')
            commit('AUTH_ERROR')
            reject(e)
          })
      })
    },
    AUTH_LOGOUT: ({commit}) => {
      return new Promise((resolve, reject) => {
        commit('CLEAR_STORAGE')
        commit('AUTH_LOGOUT')
        delete axios.defaults.headers.common['Authorization']
        resolve()
      })
    },
    REGISTER: (context, user) => {
      return new Promise((resolve, reject) => {
        axios.post('http://auth:9999/register', qs.stringify(user), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
          .then(response => resolve())
          .catch(e => reject(e))
      })
    },
    IS_NOT_EXIST: (context, username) => {
      return new Promise((resolve, reject) => {
        axios.get('http://auth:9999/check', { params: {user: username} })
          .then(response => resolve(response.data))
          .catch(e => reject(e))
      })
    },
    UPDATE_TOKENS: (context, refreshToken) => {
      return new Promise((resolve, reject) => {
        axios.post('http://auth:9999/auth/refresh', refreshToken)
          .then(response => {
            resolve(response.data)
          })
          .catch(e => reject(e))
      })
    },
    TRY_TO_REFRESH: (context) => {
      return new Promise((resolve, reject) => {
        let time = localStorage.getItem('expired-in')
        if (time && moment(time, 'x') < moment().utc()) {
          let refresh = localStorage.getItem('refresh-token')
          context.dispatch('UPDATE_TOKENS', refresh)
            .then(data => {
              let accessToken = data.accessToken
              axios.defaults.headers.common['Authorization'] = 'Bearer ' + accessToken
              context.commit('AUTH_SUCCESS', accessToken)
              localStorage.setItem('user-token', accessToken) // store the token in localstorage
              localStorage.setItem('refresh-token', data.refreshToken)
              localStorage.setItem('expired-in', data.accessTokenExpiredIn)
              resolve()
            })
            .catch(e => reject(e))
        } else {
          resolve()
        }
      })
    }
  }
})
