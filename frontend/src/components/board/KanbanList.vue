<template>
  <div id="kanban-list">
    <h2>This is the List</h2>
    <p>{{ title }}</p>
    <p>{{ num }}</p>
  <button @click="$emit('remove')">remove</button>
  </div>
</template>

<script>
export default {
  props: {
    title: String,
    num: Number
  },
  methods: {
    remove: function () {
      alert('deleted')
    }
  },
  name: 'KanbanList'
}
</script>

<style scoped>
#kanban-list {
  border: 1px solid green;
  width: 300px;
  display: inline-block;
  text-align: center;
}
</style>
