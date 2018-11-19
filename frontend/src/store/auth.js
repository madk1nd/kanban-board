import axios from 'axios'
import {authUrl, host} from '../main'
import qs from 'qs'

export default {
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
        commit('AUTH_REQUEST')
        axios.post(`http://${host}:${authUrl}/auth/login`, qs.stringify(user), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
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
        axios.post(`http://${host}:${authUrl}/auth/register`, qs.stringify(user), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
          .then(() => resolve())
          .catch(e => reject(e))
      })
    },
    IS_NOT_EXIST: (context, username) => {
      return new Promise((resolve, reject) => {
        axios.get(`http://${host}:${authUrl}/auth/check`, { params: {user: username} })
          .then(response => resolve(response.data))
          .catch(e => reject(e))
      })
    }
  }
}
