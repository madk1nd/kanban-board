<template lang="pug">
  section.section
    .container.form
      .card.radius-top
        .card-header.radius-top.notification.has-text-centered.has-background-info
          .container
            p.title.has-text-white Please login
        .card-content
          Input(type="text" placeholder="Enter username" v-model="username" autofocus)
            AccountIcon(slot="left")
          Input(type="password" placeholder="Enter password" v-model="password")
            LockIcon(slot="left")
          .field
            .control.has-text-centered
                label.checkbox
                  input(type="checkbox" tabindex="-1")
                  | &nbsp;Remember me
          .field
            .control
              button.button.is-info.is-medium.width-full(@click="login") Login
          .notification(v-if="error") {{ this.error }}
      .card-footer
        nav.breadcrumb.has-bullet-separator.is-centered.width-full.top-pad(
          aria-label="breadcrumbs"
        )
            ul
              li
                router-link(to="/register") Sign in
              li
                a(href="#") Forgot password?
              li
                a(href="#") Help
</template>
<script>
import AccountIcon from 'vue-material-design-icons/Account.vue'
import LockIcon from 'vue-material-design-icons/Lock.vue'
import Input from '@/components/Input'

export default {
  name: 'Login',
  components: { AccountIcon, LockIcon, Input },
  data: () => ({
    username: '',
    password: '',
    error: false
  }),
  methods: {
    login () {
      const {username, password} = this
      this.$store.dispatch('AUTHENTICATE', {username, password})
        .then(() => {
          this.$router.push('/board')
        })
        .catch(e => {
          this.error = e.response.data
        })
    }
  }
}
</script>
<style scoped>
.top-pad {
  padding-top: 10px;
}
.form {
  max-width: 400px;
}
</style>
