<template lang="pug">
  section.hero.is-small.is-info.is-bold.has-text-left
    .hero-head
      header.navbar
        .container
          .navbar-brand
            span.navbar-burger.burger(data-target="navbarMenuHeroC")
              span
              span
              span
          .navbarMenuHeroC.navbar-menu
            .navbar-end
              a.navbar-item.is-active Home
              a.navbar-item Examples
              a.navbar-item Documentation
              span.navbar-item(v-if="isAuthenticated")
                a.button.is-success.is-inverted(@click="logout")
                  span Logout
    .hero-body
      .container
        h1.title Kanban Board
        h2.subtitle Simple kanban flow
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
