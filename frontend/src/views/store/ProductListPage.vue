<template>
  <div style="display: grid; grid-template-columns: 280px 1fr; gap: 24px">
    <aside class="panel" style="padding: 24px">
      <h2 class="brand-title" style="font-size: 34px; margin: 0 0 16px">Filters</h2>
      <div style="display: grid; gap: 12px">
        <button class="chip" :class="{ active: !activeCategory }" @click="selectCategory(null)">全部</button>
        <button
          v-for="category in categories"
          :key="category.id"
          class="chip"
          :class="{ active: activeCategory === category.id }"
          @click="selectCategory(category.id)"
        >
          {{ category.name }}
        </button>
      </div>
    </aside>

    <section style="display: grid; gap: 22px">
      <div class="panel" style="display: flex; align-items: center; gap: 14px; padding: 20px">
        <div>
          <h1 class="section-title">New Season Essentials</h1>
          <p class="section-subtitle">后端商品列表接口：分页、分类筛选、上架状态过滤。</p>
        </div>
        <input v-model="keyword" class="input" style="max-width: 260px; margin-left: auto" placeholder="搜索商品" @keyup.enter="loadProducts" />
        <button class="btn primary" @click="loadProducts">搜索</button>
      </div>
      <p v-if="error" class="section-subtitle">{{ error }}</p>
      <div class="grid cols-3">
        <ProductCard v-for="product in products" :key="product.id" :product="product" @add-cart="addCart" />
      </div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import ProductCard from "../../components/ProductCard.vue";
import { cartApi, storeApi } from "../../services/api";

const categories = ref([]);
const products = ref([]);
const activeCategory = ref(null);
const keyword = ref("");
const error = ref("");

async function loadCategories() {
  categories.value = await storeApi.getCategories();
}

async function loadProducts() {
  try {
    const page = await storeApi.getProducts({
      categoryId: activeCategory.value,
      keyword: keyword.value,
      pageNum: 1,
      pageSize: 12
    });
    products.value = page?.list || [];
  } catch (err) {
    error.value = err.message;
  }
}

function selectCategory(id) {
  activeCategory.value = id;
  loadProducts();
}

async function addCart(product) {
  try {
    await cartApi.addCart({ productId: product.id, quantity: 1 });
    ElMessage.success("已加入购物袋");
  } catch (err) {
    ElMessage.warning(err.message || "请先登录后再加入购物车");
  }
}

onMounted(async () => {
  await loadCategories();
  await loadProducts();
});
</script>
