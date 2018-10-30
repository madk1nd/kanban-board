<template>
  <div class="kanban-board">
    <h1>Hello, {{ name }}</h1>
    <div class="board-add-container">
      <div class="board-button-add" v-show="!clicked" @click="enterListName()">
        <p>+</p>
      </div>
      <input ref="titleInput" type="text" v-show="clicked" @keyup.enter="create()"
        @keyup.escape="onFocusLost()" @focusout="onFocusLost()" v-model="newList"
        placeholder="Enter new list name..."
      >
    </div>
    <div id="board">
      <draggable class="kanban-draggable-list"
                 v-model="lists"
                 @start="drag=true" @end="onDragEnd">
        <kanban-list v-for="(list, index) in lists"
                     :key="list.id"
                     :title="list.title"
                     :num="list.number"
                     @remove="del(index, list.id)"
        ></kanban-list>
      </draggable>
    </div>
  </div>
</template>

<script>
import KanbanList from '@/components/board/KanbanList'
import axios from 'axios'
import draggable from 'vuedraggable'

export default {
  components: { KanbanList, draggable },
  name: 'KanbanBoard',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App!',
      lists: [],
      newList: '',
      clicked: false,
      name: 'Sergey',
      // TODO: remove hardcoded value
      board_id: '5bafe37fb23269aba17efe61'
    }
  },
  created () {
    axios
      .get('/api/list/all', { params: { board: this.board_id } })
      .then(response => {
        this.lists = response.data[0].lists.sort((a, b) => a.ordinal - b.ordinal)
      })
      .catch(error => console.log(error))
  },
  methods: {
    onDragEnd: function (event) {
      let asc = event.oldIndex < event.newIndex
      let start = asc ? event.oldIndex : event.newIndex
      let end = (asc ? event.newIndex : event.oldIndex) + 1
      let ordinals = this.lists
        .slice(start, end)
        .map(o => o.ordinal)
        .sort()
      for (let i = start; i < end; i++) {
        this.lists[i].ordinal = ordinals[i - start]
      }
      this.drag = false
      axios.put('/api/list/update', this.lists, { params: { board: this.board_id } })
        .then(response => response)
        .catch(error => console.log(error))
    },
    onFocusLost: function () {
      this.clicked = false
      this.newList = ''
    },
    enterListName: function () {
      this.clicked = true
      this.$nextTick(() => this.$refs.titleInput.focus())
    },
    create: function () {
      let max = Math.max(...this.lists.map(list => list.ordinal), 1) + 1
      axios.post('/api/list/add', {}, { params: { board: this.board_id, ordinal: max, title: this.newList } })
        .then(response => { this.lists.push(response.data) })
        .catch(error => console.log(error))
      this.clicked = false
      this.newList = ''
    },
    del: function (idx, removedId) {
      axios.delete('/api/list/delete', { params: { board: this.board_id, id: removedId } })
        .then(response => { this.lists.splice(idx, 1) })
        .catch(error => console.log(error))
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.kanban-board {
  border: 1px solid red;
}
#board {
  margin: 10px;
  border: 1px solid blue;
  overflow-x: auto;
  white-space: nowrap;
  text-align: left;
}
.board-button-add {
  background-color: #aaffaa;
  font-family: Arial, "Helvetica Neue", Helvetica, sans-serif;
  width: 60px;
  height: 60px;
  border-radius: 30px;
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
  transition: 0.3s;
  line-height: 60px;
  margin: 0 auto;
}
.board-button-add:active {
  background-color: #000041;
  transform: translateY(2px);
}
.board-add-container {
  height: 60px;
  vertical-align: middle;
}
input {
  margin-top: 20px;
}
</style>
