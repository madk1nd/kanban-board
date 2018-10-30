<template>
  <section class="section">
    <div class="container form">
      <div class="card radius-top">
        <div class="card-header radius-top notification has-text-centered has-background-info">
          <div class="container">
            <p class="title has-text-white">Please login</p>
          </div>
        </div>
        <div class="card-content">
          <div class="field">
            <div class="control has-icons-left">
              <input class="input is-medium" type="text" placeholder="Enter username"
                     v-model.lazy="username"
                     autofocus>
              <span class="icon is-left bottom-pad">
                    <account-icon/>
                  </span>
            </div>
          </div>
          <div class="field">
            <div class="control has-icons-left">
              <input class="input is-medium" type="password" placeholder="Enter password"
                     v-model.lazy="password">
              <span class="icon is-left bottom-pad">
                    <lock-icon/>
                  </span>
            </div>
          </div>
          <div class="field">
            <div class="control has-text-centered">
                <label class="checkbox">
                  <input type="checkbox" tabindex="-1"> Remember me
                </label>
            </div>
          </div>
          <div class="field">
            <div class="control">
              <button class="button is-info is-medium width-full" @click="login">Login</button>
            </div>
          </div>
          <div class="notification" v-if="error">{{ this.error }}</div>
        </div>
      </div>
      <div class="card-footer">
        <nav class="breadcrumb has-bullet-separator is-centered width-full top-pad" aria-label="breadcrumbs">
            <ul>
              <li><router-link to="/register">Sign in</router-link></li>
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
          this.error = e.response.data
        })
    }
  }
}
</script>
<style scoped>
.bottom-pad {
  padding-bottom: 7px;
}
.top-pad {
  padding-top: 10px;
}
.form {
  max-width: 400px;
}
</style>
