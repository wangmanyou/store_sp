<template>
  <RouterLink class="brand-logo" :to="to" :class="{ compact }">
    <span class="brand-logo-mark">A</span>
    <span class="brand-logo-copy">
      <strong>Aureline</strong>
      <small v-if="subtitle">{{ subtitle }}</small>
    </span>
  </RouterLink>
</template>

<script setup>
defineProps({
  to: {
    type: String,
    default: "/"
  },
  subtitle: {
    type: String,
    default: ""
  },
  compact: {
    type: Boolean,
    default: false
  }
});
</script>
