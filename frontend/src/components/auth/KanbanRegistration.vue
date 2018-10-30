<template>
  <section class="section">
    <div class="container form">
      <div class="card radius-top">
        <div class="card-header radius-top notification has-text-centered has-background-info">
          <div class="container">
            <p class="title has-text-white">Create new account</p>
          </div>
        </div>
        <div class="card-content">
          <div class="field">
            <label class="label has-text-left">Name</label>
            <div class="control has-icons-left has-icons-right">
              <input class="input is-medium" type="text" placeholder="For example, Fox Mulder"
                     v-model.lazy="name"
                     :class="{'is-danger': error.name.message, 'is-success': error.name.valid}"
                     autofocus>
              <span class="icon is-left bottom-pad">
                    <account-icon/>
              </span>
              <span class="icon is-medium is-right bottom-pad">
                <check-icon class="has-text-primary" v-if="error.name.valid"/>
              </span>
            </div>
            <p class="help is-danger has-text-left" v-if="error.name.message">{{ this.error.name.message }}</p>
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
              <span class="icon is-small is-left bottom-pad">
                <email-icon/>
              </span>
              <span class="icon is-medium is-right bottom-pad">
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
              <span class="icon is-large is-left bottom-pad">
                <lock-icon/>
              </span>
              <span class="icon is-medium is-right bottom-pad">
                <check-icon class="has-text-primary" v-if="error.password.valid"/>
              </span>
            </div>
            <p class="help is-danger has-text-left" v-if="error.password.message">{{ this.error.password.message }}</p>
          </div>
          <div class="field top-pad">
            <div class="control">
              <button class="button is-info is-medium width-full" @click="register">Register</button>
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
    name: '',
    password: '',
    email: '',
    error: {
      name: {
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
    'name': 'validateName',
    'email': 'validateEmail',
    'password': 'validatePassword'
  },
  methods: {
    register () {
      this.validateName(this.name)
      this.validateEmail(this.email)
      this.validatePassword(this.password)
      if (Object.keys(this.error).every(key => this.error[key].valid)) {
        this.$store.dispatch('IS_NOT_EXIST', this.email)
          .then(isNotExist => {
            if (isNotExist) {
              const {name, password, email} = this
              this.$store.dispatch('REGISTER', {name, password, email})
                .then(response => {
                  this.$router.push({name: 'KanbanConfirmation', params: {email: this.email}})
                })
                .catch(e => console.log(e))
            } else {
              this.error.email.message = 'Email already exist'
              this.error.email.valid = false
            }
          })
          .catch(e => console.log(e))
      }
    },
    validateName (name) {
      if (name.length < 3 || name.length > 50) {
        this.error.name.message = 'Name must be between 3 and 50'
        this.error.name.valid = false
      } else {
        this.error.name.message = ''
        this.error.name.valid = true
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
.bottom-pad {
  padding-bottom: 7px;
}
.top-pad {
  padding-top: 20px;
}
.form {
  max-width: 600px;
}
</style>
