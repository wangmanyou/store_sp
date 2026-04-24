<template>
  <article class="panel" style="padding: 18px; border-radius: 26px">
    <div class="product-cover visual-card" :style="{ background: coverStyle(product.coverImage || product.cover) }"></div>
    <div style="padding-top: 16px; display: grid; gap: 8px">
      <h3 class="brand-title" style="font-size: 28px; margin: 0">{{ product.name }}</h3>
      <p class="section-subtitle">{{ product.subtitle || product.categoryName || "精选生活方式商品" }}</p>
      <strong style="font-size: 16px">￥{{ money(product.price) }}</strong>
    </div>
    <div style="display: flex; gap: 10px; padding-top: 16px">
      <RouterLink class="btn secondary" :to="`/product/${product.id}`">查看</RouterLink>
      <button class="btn primary" @click="$emit('add-cart', product)">加入购物袋</button>
    </div>
  </article>
</template>

<script setup>
import { coverStyle, money } from "../services/format";

defineEmits(["add-cart"]);

defineProps({
  product: {
    type: Object,
    required: true
  }
});
</script>
