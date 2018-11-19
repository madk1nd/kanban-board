<template lang="pug">
  section.section
    .container
      h1.title Boards
      h2.subtitle Hello User
    section.section
      .container
        .columns.is-multiline
          .column.is-one-quarter
            a.has-background-warning.box(@click="startAdding") add new board
          .column.is-one-quarter.pos-abs(v-for="board in boards")
            router-link.has-background-light.box(
              :to="{ name: 'Kanban', query: { id: board.id }}"
            )
              | {{ board.name }}
              button.delete.top-right(@click.prevent="removeBoard(board.id)")
          .column.is-one-quarter(v-if="edit")
            input.input(
              type="text"
              v-model="boardName"
              @keyup.enter="addNew($event.target.value)"
              @focusout="abort"
              v-focus="true"
            )
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'Board',
  directives: {
    focus: {
      inserted: function (el) {
        el.focus()
      }
    }
  },
  data: () => ({
    edit: false,
    boardName: ''
  }),
  mounted () {
    this.fetchBoards()
  },
  computed: {
    ...mapState({
      boards: state => state.board.boards
    })
  },
  methods: {
    ...mapActions({
      fetchBoards: 'FETCH_BOARDS',
      addBoard: 'ADD_BOARD',
      deleteBoard: 'DELETE_BOARD'
    }),
    removeBoard (id) {
      this.deleteBoard(id)
    },
    startAdding () {
      this.edit = true
    },
    addNew (boardName) {
      this.edit = false
      this.boardName = ''
      this.addBoard(boardName.trim())
    },
    abort () {
      this.edit = false
      this.boardName = ''
    }
  }
}
</script>

<style scoped>
.column a {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  word-break: break-word;
}

.top-right {
  position: absolute;
  right: 20px;
  top: 20px;
}

.pos-abs {
  position: relative;
}
</style>
