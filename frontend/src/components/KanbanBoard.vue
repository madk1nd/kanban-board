<template>
  <div id="kanban-board">
    <h1>Hello, {{ name }}</h1>
    <div class="board-button-add" @click="create(lists)"><p>+</p></div>
    <div id="board">
      <kanban-list v-for="(list, index) in lists"
                   :key="list.id"
                   :title="list.title"
                   :num="list.number"
                   @remove="del(index, list.id)"
      ></kanban-list>
    </div>
  </div>
</template>

<script>
import KanbanList from '@/components/board/KanbanList'
import axios from 'axios'

export default {
  components: { KanbanList },
  name: 'KanbanBoard',
  methods: {
    create: function () {
      axios.put('http://localhost:8888/api/list/add', {}, { params: { ordinal: this.listCount + 1 } })
        .then(response => {
          this.lists.push(response.data)
          this.listCount++
        })
        .catch(error => console.log(error))
    },
    del: function (idx, removedId) {
      console.log('idx:' + idx)
      console.log('removedId:' + removedId)
      axios.delete('http://localhost:8888/api/list/delete', { params: { id: removedId } })
        .then(response => {
          this.lists.splice(idx, 1)
          this.listCount--
          console.log('getResponse')
        })
        .catch(error => console.log(error))
    }
  },
  data () {
    return {
      msg: 'Welcome to Your Vue.js App!',
      lists: [],
      listCount: 0,
      name: 'Sergey'
    }
  },
  created () {
    axios
      .post('http://localhost:8888/api/list/all', {}, { params: { userId: 'admin' } })
      .then(response => {
        this.lists = response.data
        this.listCount = response.data.length
      })
      .catch(error => console.log(error))
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#kanban-board {
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
p {
}
</style>
