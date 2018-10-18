<template>
  <section class="hero is-small is-info is-bold has-text-left">
    <!-- Hero head: will stick at the top -->
    <div class="hero-head">
      <header class="navbar">
        <div class="container">
          <div class="navbar-brand">
            <span class="navbar-burger burger" data-target="navbarMenuHeroC">
              <span></span>
              <span></span>
              <span></span>
            </span>
          </div>
          <div id="navbarMenuHeroC" class="navbar-menu">
            <div class="navbar-end">
              <a class="navbar-item is-active">
                Home
              </a>
              <a class="navbar-item">
                Examples
              </a>
              <a class="navbar-item">
                Documentation
              </a>
              <span class="navbar-item" v-if="isAuthenticated" >
              <a class="button is-success is-inverted" @click="logout">
                <span>Logout</span>
              </a>
            </span>
            </div>
          </div>
        </div>
      </header>
    </div>
    <div class="hero-body">
      <div class="container">
        <h1 class="title">Kanban Board</h1>
        <h2 class="subtitle">Simple kanban flow</h2>
      </div>
    </div>
  </section>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'KanbanHeader',
  methods: {
    logout () {
      this.$store.dispatch('AUTH_LOGOUT')
        .then(() => {
          this.$router.push('/')
        })
    }
  },
  computed: {
    ...mapGetters(['isAuthenticated'])
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .kanban-header {}
  .top {
    padding-bottom:1px;
    padding-right: 5px;
  }
</style>
