<template>
  <aside class="panel" style="padding: 26px; min-height: calc(100vh - 48px)">
    <div class="brand-title" style="font-size: 32px; font-weight: 700; margin-bottom: 24px">
      Aureline Admin
    </div>
    <nav style="display: grid; gap: 10px">
      <RouterLink v-for="item in items" :key="item.to" :to="item.to" class="chip" :class="{ active: route.path === item.to }">
        {{ item.label }}
      </RouterLink>
    </nav>
  </aside>
</template>

<script setup>
import { useRoute } from "vue-router";

const route = useRoute();

const items = [
  { label: "总览", to: "/admin" },
  { label: "分类管理", to: "/admin/categories" },
  { label: "Banner管理", to: "/admin/banners" },
  { label: "商品管理", to: "/admin/products" },
  { label: "订单管理", to: "/admin/orders" },
  { label: "用户管理", to: "/admin/users" }
];
</script>
