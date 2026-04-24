<template>
  <div style="display: grid; gap: 28px">
    <section class="grid cols-2">
      <div class="hero-card panel" style="padding: 42px; background: linear-gradient(135deg, #d8c0aa, #f4e7d7)">
        <span class="chip" style="width: fit-content; background: #f4e9dd; border: none; color: #5b4332">
          SPRING 2026 COLLECTION
        </span>
        <h1 class="brand-title" style="font-size: 68px; line-height: 0.95; margin: 22px 0 16px">
          更像真实品牌网站的商城首页
        </h1>
        <p class="section-subtitle" style="max-width: 560px">
          用 Banner、商品系列和品牌叙事组织页面，而不是只把数据库字段展示出来。
        </p>
        <div style="display: flex; gap: 12px; padding-top: 18px">
          <RouterLink class="btn primary" to="/products">浏览新品</RouterLink>
          <button class="btn secondary">查看专题</button>
        </div>
      </div>
      <div style="display: grid; gap: 20px">
        <div
          class="hero-card panel"
          :style="{ minHeight: '340px', padding: '28px', background: coverStyle(mainBanner?.imageUrl) }"
        >
          <div class="brand-title" style="font-size: 42px; margin-top: 190px; color: #fff7ee">
            {{ mainBanner?.title || "Campaign Visual" }}
          </div>
        </div>
        <div class="panel" style="padding: 24px">
          <h2 class="brand-title" style="font-size: 34px; margin: 0 0 12px">本周编辑推荐</h2>
          <div style="display: flex; gap: 10px; flex-wrap: wrap; margin-bottom: 14px">
            <span v-for="category in categories.slice(0, 3)" :key="category.id" class="chip active">{{ category.name }}</span>
          </div>
          <p class="section-subtitle">首页除了卖货，也要有内容栏目和品牌语气。</p>
        </div>
      </div>
    </section>

    <section>
      <div style="display: flex; align-items: end; justify-content: space-between; margin-bottom: 18px">
        <div>
          <h2 class="section-title">精选系列</h2>
          <p class="section-subtitle">数据来自后端商品接口，分类名称由商品列表关联查询返回。</p>
        </div>
      </div>
      <p v-if="error" class="section-subtitle">{{ error }}</p>
      <div class="grid cols-4">
        <ProductCard v-for="product in products" :key="product.id" :product="product" @add-cart="addCart" />
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import ProductCard from "../../components/ProductCard.vue";
import { cartApi, storeApi } from "../../services/api";
import { coverStyle } from "../../services/format";

const banners = ref([]);
const categories = ref([]);
const products = ref([]);
const error = ref("");

const mainBanner = computed(() => banners.value[0]);

async function loadHomeData() {
  try {
    const [bannerData, categoryData, productPage] = await Promise.all([
      storeApi.getBanners(),
      storeApi.getCategories(),
      storeApi.getProducts({ pageNum: 1, pageSize: 8 })
    ]);
    banners.value = bannerData || [];
    categories.value = categoryData || [];
    products.value = productPage?.list || [];
  } catch (err) {
    error.value = err.message;
  }
}

async function addCart(product) {
  try {
    await cartApi.addCart({ productId: product.id, quantity: 1 });
    ElMessage.success("已加入购物袋");
  } catch (err) {
    ElMessage.warning(err.message || "请先登录后再加入购物车");
  }
}

onMounted(loadHomeData);
</script>
