<template lang="pug">
  section.section
    .container.form
      .card.radius-top
        .card-header.radius-top.notification.has-text-centered.has-background-info
          .container
            p.title.has-text-white Check your email address
        .card-content
          span.subtitle
            p We sent an email to&nbsp;
            a(href="#") {{ email }}
            p Please click the button in the email to continue.
            hr
            p Could not find? Try to send it again
          .field.top-pad
            .control
              button.button.is-info.is-medium.width-full(@click="send") Send
</template>

<script>
export default {
  name: 'KanbanConfirmation',
  props: ['email'],
  methods: {
    send () {
      // not implemented
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
