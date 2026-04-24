<template>
  <div class="grid cols-2">
    <section style="display: grid; gap: 18px">
      <div class="panel visual-card" :style="{ minHeight: '620px', background: coverStyle(product.coverImage), borderRadius: '34px' }"></div>
      <div class="grid" style="grid-template-columns: repeat(4, 1fr)">
        <div v-for="index in 4" :key="index" class="panel" :style="{ minHeight: '120px', background: coverStyle(product.coverImage), opacity: 0.8 }"></div>
      </div>
    </section>

    <section class="panel" style="padding: 30px; display: grid; gap: 18px">
      <span class="chip" style="width: fit-content; background: #f1e5d8; border: none; color: #6b4e39">LIMITED EDITION</span>
      <h1 class="brand-title" style="font-size: 50px; line-height: 0.95; margin: 0">{{ product.name }}</h1>
      <p class="section-subtitle">{{ product.subtitle || "精选商品详情" }}</p>
      <strong style="font-size: 22px">￥{{ money(product.price) }}</strong>

      <div class="soft-panel" style="padding: 18px; display: grid; gap: 12px">
        <span>库存：{{ product.stock }}</span>
        <span>销量：{{ product.sales }}</span>
        <span>分类：{{ product.categoryName }}</span>
        <span>配送：24 小时内发货，支持 7 天无理由</span>
      </div>

      <div style="display: flex; align-items: center; gap: 14px">
        <strong>数量</strong>
        <el-input-number v-model="quantity" class="brand-number" :min="1" :max="product.stock || 999" />
      </div>

      <div style="display: flex; gap: 12px">
        <button class="btn primary" @click="addCart">加入购物袋</button>
        <button class="btn secondary">收藏</button>
      </div>

      <div class="panel" style="padding: 18px">
        <h2 class="brand-title" style="font-size: 30px; margin: 0 0 10px">商品详情</h2>
        <p class="section-subtitle">{{ product.detail || "暂无详情介绍" }}</p>
      </div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import { useRoute } from "vue-router";
import { cartApi, storeApi } from "../../services/api";
import { coverStyle, money } from "../../services/format";

const route = useRoute();
const product = ref({});
const quantity = ref(1);

async function loadDetail() {
  product.value = await storeApi.getProductDetail(route.params.id);
}

async function addCart() {
  try {
    await cartApi.addCart({ productId: product.value.id, quantity: quantity.value });
    ElMessage.success("已加入购物袋");
  } catch (err) {
    ElMessage.warning(err.message || "请先登录后再加入购物车");
  }
}

onMounted(loadDetail);
</script>
