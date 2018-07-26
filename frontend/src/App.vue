<template>
  <div id="app">
    <kanban-header></kanban-header>
    <router-view/>
    <kanban-footer></kanban-footer>
  </div>
</template>

<script>
import KanbanHeader from '@/components/KanbanHeader'
import KanbanFooter from '@/components/KanbanFooter'

export default {
  name: 'App',
  components: {
    'kanban-header': KanbanHeader,
    'kanban-footer': KanbanFooter
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  border: 1px solid black;
}
button {
  background-color: white;
  color: black;
  border: 1px solid black;
}
button:hover {
  background-color:  #f9cd0b;
  border-color: black;
}
</style>
