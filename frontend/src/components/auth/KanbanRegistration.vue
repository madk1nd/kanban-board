<template>
  <section class="section">
    <div class="container" style="max-width: 600px;">
      <div class="card" style="-webkit-border-radius: 5px; -moz-border-radius: 5px;border-radius: 5px;">
        <div class="card-header notification has-text-centered has-background-info">
          <div class="container">
            <p class="title has-text-white">Create new account</p>
          </div>
        </div>
        <div class="card-content">
          <div class="field">
            <label class="label has-text-left">Username</label>
            <div class="control has-icons-left has-icons-right">
              <input class="input is-medium" type="text" placeholder="For example, Fox Mulder"
                     v-model.lazy="username"
                     :class="{'is-danger': error.username.message, 'is-success': error.username.valid}"
                     autofocus>
              <span class="icon is-left pad-bottom">
                    <account-icon/>
              </span>
              <span class="icon is-medium is-right pad-bottom">
                <check-icon class="has-text-primary" v-if="error.username.valid"/>
              </span>
            </div>
            <p class="help is-danger has-text-left" v-if="error.username.message">{{ this.error.username.message }}</p>
          </div>
          <div class="field">
            <label class="label has-text-left">Email</label>
            <div class="control has-icons-left has-icons-right">
              <input
                class="input is-medium"
                :class="{'is-danger': error.email.message, 'is-success': error.email.valid}"
                type="email"
                placeholder="For example, fox.mulder@example.com"
                v-model.lazy="email">
              <span class="icon is-small is-left pad-bottom">
                <email-icon/>
              </span>
              <span class="icon is-medium is-right pad-bottom">
                <check-icon class="has-text-primary" v-if="error.email.valid"/>
              </span>
            </div>
            <p class="help is-danger has-text-left" v-if="error.email.message">{{ this.error.email.message }}</p>
          </div>
          <div class="field">
            <label class="label has-text-left">Password</label>
            <div class="control has-icons-left has-icons-right">
              <input class="input is-medium"
                     :class="{'is-danger': error.password.message, 'is-success': error.password.valid}"
                     type="password"
                     placeholder="For example, ********"
                     v-model.lazy="password">
              <span class="icon is-large is-left pad-bottom">
                <lock-icon/>
              </span>
              <span class="icon is-medium is-right pad-bottom">
                <check-icon class="has-text-primary" v-if="error.password.valid"/>
              </span>
            </div>
            <p class="help is-danger has-text-left" v-if="error.password.message">{{ this.error.password.message }}</p>
          </div>
          <div class="field" style="padding-top: 20px;">
            <div class="control">
              <button class="button is-info is-medium" style="width: 100%;" @click="register">Register</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import AccountIcon from 'vue-material-design-icons/Account.vue'
import LockIcon from 'vue-material-design-icons/Lock.vue'
import EmailIcon from 'vue-material-design-icons/Email.vue'
import CheckIcon from 'vue-material-design-icons/Check.vue'

export default {
  name: 'KanbanRegistration',
  components: { CheckIcon, AccountIcon, LockIcon, EmailIcon },
  data: () => ({
    username: '',
    password: '',
    email: '',
    error: {
      username: {
        message: '',
        valid: false
      },
      email: {
        message: '',
        valid: false
      },
      password: {
        message: '',
        valid: false
      }
    }
  }),
  watch: {
    'username': 'validateUsername',
    'email': 'validateEmail',
    'password': 'validatePassword'
  },
  methods: {
    register () {
      console.log('Try to register user')
      this.validateUsername(this.username)
      this.validateEmail(this.email)
      this.validatePassword(this.password)
      if (Object.keys(this.error).every(key => this.error[key].valid)) {
        console.log('all fine')
        this.$router.push('/')
      } else {
        console.log('we have errors in form')
      }
    },
    validateUsername (name) {
      if (name.length < 3 || name.length > 50) {
        this.error.username.message = 'Username must be between 3 and 50'
        this.error.username.valid = false
      } else {
        this.error.username.message = ''
        this.error.username.valid = true
      }
    },
    validatePassword (pass) {
      let re = /^(?=.*[\d])(?=.*[A-Z])[\w!@#$%^&*]{8,}$/
      if (pass.length < 8) {
        this.error.password.message = 'Password must contain at least 8 characters'
        this.error.password.valid = false
      } else if (!re.test(pass)) {
        this.error.password.message = 'Password must contain at least one number and one uppercase characters'
        this.error.password.valid = false
      } else {
        this.error.password.message = ''
        this.error.password.valid = true
      }
    },
    validateEmail (email) {
      // eslint-disable-next-line
      let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      if (!re.test(email)) {
        this.error.email.message = 'Email address is invalid'
        this.error.email.valid = false
      } else {
        this.error.email.message = ''
        this.error.email.valid = true
      }
    }
  }
}
</script>

<style scoped>
.pad-bottom {
  padding-bottom: 5px;
}
</style>
