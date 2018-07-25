<template>
  <div id="kanban-board">
    <h1>Hello, {{ name }}</h1>
    <div class="board-button-add" @click="create(lists)"><p>+</p></div>
    <div id="board">
      <kanban-list v-for="(list, index) in lists"
                   :key="list.id"
                   :title="list.title"
                   :num="list.number"
                   v-on:remove="lists.splice(index, 1)"
      ></kanban-list>
    </div>
  </div>
</template>

<script>
import KanbanList from '@/components/board/KanbanList'

export default {
  components: { KanbanList },
  name: 'KanbanBoard',
  methods: {
    create: function () {
      let id = ++this.counter
      this.lists.push(
        {
          id: id,
          number: id,
          title: 'new'
        }
      )
    }
  },
  data () {
    return {
      msg: 'Welcome to Your Vue.js App!',
      lists: [
        {
          id: 1,
          number: 123,
          title: 'hello'
        }
      ],
      counter: 1,
      name: 'Sergey'
    }
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
