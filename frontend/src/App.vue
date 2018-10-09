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
}
.radius-top {
  -webkit-border-radius: 5px 5px 0 0;
  -moz-border-radius: 5px 5px 0 0;
  border-radius: 5px 5px 0 0;
}
.width-full {
  width: 100%;
}
</style>
