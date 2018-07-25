<template>
  <div class="kanban-task">
    <div class="task-text">
      {{ text }}
    </div>
    <div class="task-button-remove">
      <button @click="$emit('remove')">X</button>
    </div>
  </div>
</template>

<script>
export default {
  props: ['text'],
  name: 'KanbanTask'
}
</script>

<style scoped>
* {
  margin: 5px;
  border-radius: 5px;
}
.kanban-task {
  background-color: #aaffaa;
  box-shadow: 0 2px 4px 0 rgba(0,0,0,0.2);
}
.kanban-task:hover {
  box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
}
.task-text {
  padding: 5px 10px;
  width: 160px;
  display: inline-block;
  word-wrap: break-word;
  white-space: normal;
  vertical-align: middle;
  text-align: justify;
}
.task-button-remove {
  width: 60px;
  padding: 2px;
  display: inline-block;
}
button {
  width: 100%;
}
</style>
