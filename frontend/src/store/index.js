import Vue from 'vue'
import Vuex from 'vuex'
import 'es6-promise/auto'
import auth from './auth'
import board from './board'

Vue.use(Vuex)

export const store = new Vuex.Store({
  modules: {
    auth,
    board
  }
})
