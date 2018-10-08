<template>
  <section class="section">
    <div class="container" style="max-width: 400px;">
      <div class="card" style="-webkit-border-radius: 5px;-moz-border-radius: 5px;border-radius: 5px;">
        <div class="card-header notification has-text-centered has-background-info">
          <div class="container">
            <p class="title has-text-white">Please login</p>
          </div>
        </div>
        <div class="card-content">
          <div class="field">
            <!--<label class="label has-text-left">Username</label>-->
            <div class="control has-icons-left">
              <input class="input is-medium" type="text" placeholder="Enter username"
                     v-model.lazy="username"
                     autofocus>
              <span class="icon is-left top">
                    <account-icon/>
                  </span>
            </div>
          </div>
          <div class="field">
            <!--<label class="label has-text-left">Password</label>-->
            <div class="control has-icons-left">
              <input class="input is-medium" type="password" placeholder="Enter password"
                     v-model.lazy="password">
              <span class="icon is-large is-left top">
                    <lock-icon/>
                  </span>
            </div>
          </div>
          <div class="field">
            <div class="control has-text-centered">
              <!--<div class="container" style="width: 100%; text-align: center">-->
                <label class="checkbox">
                  <input type="checkbox" tabindex="-1"> Remember me
                </label>
              <!--</div>-->
            </div>
          </div>
            <div class="field">
              <div class="control">
                <button class="button is-info is-medium" style="width: 100%;" @click="login">Login</button>
              </div>
            </div>
          </div>
        </div>
        <div class="card-footer">
          <nav class="breadcrumb has-bullet-separator is-centered" style="width: 100%; padding-top: 10px;" aria-label="breadcrumbs">
              <ul>
                <li><router-link to="/register">Sign in</router-link></li>
                <!--<li><a href="#">Sign in</a></li>-->
                <li><a href="#">Forgot password?</a></li>
                <li><a href="#">Help</a></li>
              </ul>
          </nav>
        </div>
      </div>
  </section>
</template>
<script>
import AccountIcon from 'vue-material-design-icons/Account.vue'
import LockIcon from 'vue-material-design-icons/Lock.vue'

export default {
  name: 'Login',
  components: { AccountIcon, LockIcon },
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
          this.error = 'Login failed! ' + e.response.data
        })
    }
  }
}
</script>
<style scoped>
.top {
  padding-bottom: 7px;
}
</style>
