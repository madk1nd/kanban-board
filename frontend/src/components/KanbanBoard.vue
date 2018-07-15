<template>
  <div id="kanban-board">
    <h1>Hello, {{ name }}</h1>
    <div id="board">
      <kanban-list></kanban-list>
      <kanban-list></kanban-list>
      <kanban-list></kanban-list>
    </div>
  </div>
</template>

<script>
import KanbanList from '@/components/board/KanbanList.vue'

export default {
  components: { KanbanList },
  name: 'KanbanBoard',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App!',
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
