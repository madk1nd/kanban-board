<template>
  <div id="kanban-board">
    <h1>Hello, {{ name }}</h1>
    <button @click="create(lists)">Add New...</button>
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
  border: 1px solid blue;
  overflow-x: auto;
  white-space: nowrap;
  text-align: left;
}
</style>
