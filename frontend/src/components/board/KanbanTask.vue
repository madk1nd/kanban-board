<template>
  <div class="kanban-task">
    <h2>{{ text }}</h2>
    <button @click="$emit('remove')">X</button>
  </div>
</template>

<script>
export default {
  props: ['text'],
  name: 'KanbanTask'
}
</script>

<style scoped>
.kanban-task {
  border: 1px solid green;
}
</style>
