<template lang="pug">
  section.section
    .container.form
      .card.radius-top
        .card-header.radius-top.notification.has-text-centered.has-background-info
          .container
            p.title.has-text-white Create new account
        .card-content
          Input(type="text" placeholder="For example, Fox Mulder" v-model="name"
            :markDanger="error.name.message" :markSuccess="error.name.valid" autofocus
          )
            | Name
            AccountIcon(slot="left")
            CheckIcon.has-text-success(slot="right" v-if="error.name.valid")
            template(slot="error")
              | {{ this.error.name.message }}
          Input(type="email" placeholder="For example, fox.mulder@example.com"
            v-model="email" :markDanger="error.email.message" :markSuccess="error.email.valid"
          )
            | Email
            EmailIcon(slot="left")
            CheckIcon.has-text-success(slot="right" v-if="error.email.valid")
            template(slot="error")
              | {{ this.error.email.message }}
          Input(type="password" placeholder="For example, ********" v-model="password"
            :markDanger="error.password.message" :markSuccess="error.password.valid"
          )
            | Password
            LockIcon(slot="left")
            CheckIcon(slot="right" v-if="error.password.valid")
            template(slot="error")
              | {{ this.error.password.message }}
          .field.top-pad
            .control
              button.button.is-info.is-medium.width-full(@click="register") Register
</template>

<script>
import AccountIcon from 'vue-material-design-icons/Account.vue'
import LockIcon from 'vue-material-design-icons/Lock.vue'
import EmailIcon from 'vue-material-design-icons/Email.vue'
import CheckIcon from 'vue-material-design-icons/Check.vue'
import Input from '@/components/Input'

export default {
  name: 'KanbanRegistration',
  components: { CheckIcon, AccountIcon, LockIcon, EmailIcon, Input },
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
.top-pad {
  padding-top: 20px;
}
.form {
  max-width: 600px;
}
</style>
