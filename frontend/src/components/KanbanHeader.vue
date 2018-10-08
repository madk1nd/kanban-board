<template>
  <section class="hero is-small is-info is-bold has-text-left">
    <!-- Hero head: will stick at the top -->
    <div class="hero-head">
      <header class="navbar">
        <div class="container">
          <div class="navbar-brand">
            <!--<a class="navbar-item">-->
              <!--<img src="https://bulma.io/images/bulma-type-white.png" alt="Logo">-->
            <!--</a>-->
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
              <span class="navbar-item">
              <a class="button is-success is-inverted">
                <span class="top">
                  <github-icon/>
                </span>
                <!--<span class="icon">-->
                  <!--<i class="fab fa-github"></i>-->
                <!--</span>-->
                <span>Download</span>
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
  <!--<div class="kanban-header footer has-background-white">-->
    <!--<h2>This is the header</h2>-->
  <!--</div>-->
</template>

<script>
import GithubIcon from 'vue-material-design-icons/GithubFace.vue'

export default {
  name: 'KanbanHeader',
  components: { GithubIcon }
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
