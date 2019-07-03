<template lang="pug">
  section.section
    .card.margins.shadow(v-for="board in boards")
      header.card-header
        p.card-header-title {{ board.name }}
        a.card-header-icon(@click="test")
          Plus.add
      .card-content.kanban-content
        .columns.is-multiline
          List(v-for="_ in 3")
</template>

<script>
import Plus from 'vue-material-design-icons/Plus.vue'
import List from '@/pages/List'
import { mapState, mapActions } from 'vuex'

export default {
  name: 'Kanban',
  components: { Plus, List },
  data: () => ({
  }),
  computed: {
    ...mapState({
      boards: state => state.board.boards
    })
  },
  mounted () {
    this.fetchBoards()
  },
  methods: {
    ...mapActions({
      fetchBoards: 'FETCH_BOARDS'
    }),
    test () {
      console.log('test')
    }
  }
}
</script>

<style scoped>
  .add {
    margin: -4px 0 0 0;
  }
  .margins {
    margin: 15px 0;
  }
  .shadow {
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
  }
  .kanban-content {
    padding: 0.1rem 0;
  }
</style>
