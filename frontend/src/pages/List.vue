<template lang="pug">
  .column.is-full
    .notification.is-marginless
      | header
      Plus.left
    .notification
      .columns.is-multiline
        Task(v-for="_ in 6")
</template>

<script>
  import Plus from 'vue-material-design-icons/Plus.vue'
  import Task from '@/pages/Task'

  export default {
    components: { Plus, Task }
  }
</script>

<style>
  .left {
    float: left;
    color: #3273dc;
  }
  .left:hover {
    cursor: pointer;
    color: #363636;
  }
</style>
