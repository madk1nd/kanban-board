<template>
  <div class="kanban-list">
    <div>
      <input type="text" placeholder="Start typing..." v-model="newtask">
      <button @click="add(newtask)">add</button>
    </div>
    <kanban-task
      v-for="(task, index) in tasks"
      :key="task.id"
      :text="task.text"
      v-on:remove="tasks.splice(index, 1)">
    </kanban-task>
    <button @click="$emit('remove')">remove</button>
  </div>
</template>

<script>
import KanbanTask from '@/components/board/KanbanTask.vue'

export default {
  components: { KanbanTask },
  props: {
    title: String,
    num: Number
  },
  name: 'KanbanList',
  methods: {
    add: function (newText) {
      if (newText === '') {
        alert('Please enter the text of the task')
        return
      }
      this.tasks.push(
        {
          id: Math.random() * 1000000,
          text: newText
        }
      )
      this.newtask = ''
    }
  },
  data () {
    return {
      counter: Math.random() * 1000000,
      newtask: '',
      tasks: [
        {
          id: 1,
          text: 'This is my first task'
        }
      ]
    }
  }
}
</script>

<style scoped>
.kanban-list {
  border: 1px solid green;
  width: 300px;
  display: inline-block;
  text-align: center;
}
</style>
