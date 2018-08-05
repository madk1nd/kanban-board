<template>
  <div class="kanban-list">
    <h2 class="kanban-list-title">{{ title }}</h2>
    <div class="kanban-add-card">
      <input type="text" placeholder="Start typing..." v-model="newtask">
      <button class="list-button-add" @click="add(newtask)">add</button>
    </div>
    <draggable class="kanban-draggable-task" v-model="tasks" :options="{group:'id'}" @start="drag=true" @end="drag=false">
      <kanban-task
        v-for="(task, index) in tasks"
        :key="task.id"
        :text="task.text"
        v-on:remove="tasks.splice(index, 1)">
      </kanban-task>
    </draggable>
    <button class="list-button-remove" @click="$emit('remove')">remove</button>
  </div>
</template>

<script>
import KanbanTask from '@/components/board/KanbanTask.vue'
import draggable from 'vuedraggable'

export default {
  components: { KanbanTask, draggable },
  props: {
    title: String,
    num: Number
  },
  name: 'KanbanList',
  methods: {
    add: function (newText) {
      if (newText === '') {
        alert('Please enter the text of the task')
      } else {
        this.tasks.push(
          {
            id: Math.random() * 1000000,
            text: newText
          }
        )
        this.newtask = ''
      }
    }
  },
  data () {
    return {
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
* {
  border-radius: 5px;
}
.kanban-list {
  background-color: cornflowerblue;
  box-shadow: 4px 4px 2px 1px rgba(0,0,0,0.2);
  margin: 10px;
  vertical-align: top;
  width: 300px;
  display: inline-block;
  text-align: center;
}
.kanban-add-card {
  margin: 10px;
}
input {
  width: 80%;
}
.list-button-add {
  width: 20%;
}
.list-button-remove {
  width: 290px;
  display: inline-block;
  padding: 5px;
  margin: 0 5px 5px 5px;
}
.kanban-draggable-task {
  border: 2px solid rgba(255, 0, 0, 0);
}
.sortable-chosen {
  border: 2px solid black;
}
</style>
