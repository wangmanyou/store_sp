<template>
  <div style="padding: 24px">
    <div style="display: grid; grid-template-columns: 280px 1fr; gap: 20px">
      <AdminSidebar />
      <main>
        <RouterView />
      </main>
    </div>
  </div>
</template>

<script setup>
import AdminSidebar from "../components/AdminSidebar.vue";
</script>
