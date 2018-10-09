<template>
  <section class="section">
    <div class="container form">
      <div class="card radius-top">
        <div class="card-header radius-top notification has-text-centered has-background-info">
          <div class="container">
            <p class="title has-text-white">Check your email address</p>
          </div>
        </div>
        <div class="card-content">
          <span class="subtitle">
            <p>We sent an email to <a href="#">{{ email }}</a></p>
            <p>Please click the button in the email to continue.</p>
            <hr>
            <p>Could not find? Try to send it again</p>
          </span>
          <div class="field top-pad">
            <div class="control">
              <button class="button is-info is-medium width-full" @click="send">Send</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'KanbanConfirmation',
  props: ['email'],
  methods: {
    send () {
      console.log(this.email)
    }
  }
}
</script>

<style scoped>
.form {
  max-width: 600px;
}
.top-pad {
  padding-top: 20px;
}
</style>
