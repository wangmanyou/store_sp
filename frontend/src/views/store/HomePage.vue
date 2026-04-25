<template>
  <div style="display: grid; gap: 34px">
    <section class="home-hero panel">
      <div class="home-hero-copy">
        <span class="chip" style="width: fit-content">SPRING 2026 COLLECTION</span>
        <h1 class="brand-title">把日常用品摆成好看的生活现场</h1>
        <p class="section-subtitle">
          以 Banner 主视觉、精选商品和分类内容组织首页，让照片成为页面的视觉重点，而不是简单贴图。
        </p>
        <div class="home-hero-actions">
          <RouterLink class="btn primary" to="/products">浏览新品</RouterLink>
          <RouterLink class="btn secondary" to="/products">查看专题</RouterLink>
        </div>
      </div>

      <div class="home-hero-visual">
        <div class="campaign-photo" :style="{ background: coverStyle(mainBanner?.imageUrl) }">
          <div class="campaign-caption">
            <span>{{ mainBanner?.subtitle || "FEATURED CAMPAIGN" }}</span>
            <strong class="brand-title">{{ mainBanner?.title || "Aureline Select" }}</strong>
          </div>
        </div>
      </div>
    </section>

    <section v-if="supportBanners.length || categories.length" class="campaign-strip">
      <article v-for="banner in supportBanners" :key="banner.id" class="campaign-tile panel">
        <div class="campaign-tile-photo" :style="{ background: coverStyle(banner.imageUrl) }"></div>
        <div>
          <h2 class="brand-title">{{ banner.title }}</h2>
          <p class="section-subtitle">{{ banner.subtitle || "精选主题视觉" }}</p>
        </div>
      </article>

      <article class="campaign-tile panel editorial-tile">
        <div>
          <span class="chip active">Editor Picks</span>
          <h2 class="brand-title">本周编辑推荐</h2>
          <div class="category-pills">
            <span v-for="category in categories.slice(0, 3)" :key="category.id" class="chip">{{ category.name }}</span>
          </div>
        </div>
      </article>
    </section>

    <section>
      <div style="display: flex; align-items: end; justify-content: space-between; margin-bottom: 18px">
        <div>
          <h2 class="section-title">精选系列</h2>
          <p class="section-subtitle">从后端商品接口读取数据，按分类、价格和库存呈现。</p>
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
const supportBanners = computed(() => banners.value.slice(1, 3));

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
    ElMessage.warning(err.message || "请先登录后再加入购物袋");
  }
}

onMounted(loadHomeData);
</script>
