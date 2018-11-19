import axios from 'axios'

export default {
  state: {
    boards: []
  },
  mutations: {
    UPDATE_BOARDS: (state, boards) => {
      state.boards = boards
    },
    PUSH_BOARD: (state, board) => {
      state.boards.push(board)
    },
    REMOVE_BOARD: (state, id) => {
      state.boards = state.boards.filter(o => o.id !== id)
    }
  },
  actions: {
    FETCH_BOARDS: async ({commit}) => {
      await axios.get('/api/boards')
        .then(({ data }) => commit('UPDATE_BOARDS', data))
        .catch(error => console.log(error))
    },
    ADD_BOARD: async ({commit}, board) => {
      await axios.post(`/api/board`, board)
        .then(({ data }) => commit('PUSH_BOARD', data))
        .catch(error => console.log(error))
    },
    DELETE_BOARD: async ({commit}, id) => {
      await axios.delete(`/api/board`, { data: id })
        .then(({ data }) => commit('REMOVE_BOARD', data))
        .catch(error => console.log(error))
    }
  }
}
