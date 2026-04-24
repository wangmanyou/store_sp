<template>
  <div>
    <StoreHeader />
    <main class="container" style="padding-bottom: 32px">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import StoreHeader from "../components/StoreHeader.vue";
</script>
