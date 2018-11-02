<template lang="pug">
  footer.footer.has-background-light.kanban-footer
    .content.has-text-centered
      p
        strong Kanban Board
        |  by &nbsp;
        a(href="https://github.com/madk1nd") Sergey Vlasov
        |. The source code is licensed&nbsp;
        a(href="http://opensource.org/licenses/mit-license.php") MIT
        |. The website content
        | is licensed&nbsp;
        a(href="http://creativecommons.org/licenses/by-nc-sa/4.0/") CC BY NC SA 4.0
        |.
</template>

<script>
export default {
  name: 'KanbanFooter'
}
</script>

<style scoped>
.kanban-footer {
  position: absolute;
  width: 100%;
  bottom: 0;
}
</style>
