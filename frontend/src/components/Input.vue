<template lang="pug">
  .field
    label.label.has-text-left(v-if="$slots.default")
      slot
    .control.has-icons-left.has-icons-right
      input.input.is-medium(
        v-bind="$attrs"
        :value="value"
        :class="{'is-danger': markDanger, 'is-success': markSuccess}"
        @focusout="$emit('input', $event.target.value)"
        @blur="$emit('input', $event.target.value)"
      )
      span.icon.is-left.bottom-pad
        slot(name="left")
      span.icon.is-medium.is-right.bottom-pad
        slot(name="right")
    p.help.is-danger.has-text-left(v-if="$slots.error")
      slot(name="error")
</template>

<script>
export default {
  name: 'Input',
  inheritAttrs: false,
  props: [ 'value', 'markDanger', 'markSuccess' ],
  data: () => ({
    test: this.value
  })
}
</script>

<style scoped>
.bottom-pad {
  padding-bottom: 7px;
}
</style>
