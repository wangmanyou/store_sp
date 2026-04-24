<template>
  <div style="display: grid; gap: 20px">
    <section class="grid cols-4">
      <article v-for="stat in stats" :key="stat.label" class="panel" style="padding: 22px">
        <div class="section-subtitle">{{ stat.label }}</div>
        <div class="brand-title" style="font-size: 42px; margin-top: 8px">{{ stat.value }}</div>
        <p class="section-subtitle" style="margin-top: 8px">{{ stat.desc }}</p>
      </article>
    </section>

    <section class="grid cols-2">
      <article class="panel" style="padding: 24px">
        <h2 class="section-title">经营概览</h2>
        <div class="soft-panel dashboard-chart">
          <div>
            <div class="brand-title" style="font-size: 36px; color: #5d5249">Real Data Overview</div>
            <p class="section-subtitle" style="margin-top: 10px">当前数据来自后端管理接口，不再使用本地 mock。</p>
          </div>
        </div>
      </article>

      <div style="display: grid; gap: 18px">
        <article class="panel" style="padding: 24px">
          <h2 class="section-title">待处理事项</h2>
          <div style="display: grid; gap: 12px; margin-top: 14px">
            <span class="chip">待发货订单 {{ pendingOrders.length }}</span>
            <span class="chip">低库存商品 {{ lowStockProducts.length }}</span>
            <span class="chip">启用 Banner {{ enabledBannerCount }}</span>
          </div>
        </article>
        <article class="panel warm-feature-card">
          <h2 class="section-title">热销单品</h2>
          <div style="display: grid; gap: 12px; margin-top: 14px">
            <div v-for="product in hotProducts" :key="product.id" class="dashboard-rank-row">
              <span>{{ product.name }}</span>
              <strong>{{ product.sales || 0 }}</strong>
            </div>
            <p v-if="!hotProducts.length" class="section-subtitle">暂无商品数据</p>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { adminApi } from "../../services/api";

const products = ref([]);
const orders = ref([]);
const pendingOrders = ref([]);
const users = ref([]);
const banners = ref([]);

const lowStockProducts = computed(() => products.value.filter((product) => Number(product.stock || 0) <= 10));
const enabledBannerCount = computed(() => banners.value.filter((banner) => banner.status === 1).length);
const hotProducts = computed(() =>
  [...products.value].sort((a, b) => Number(b.sales || 0) - Number(a.sales || 0)).slice(0, 3)
);

const stats = computed(() => [
  { label: "商品数量", value: products.value.length, desc: "来自商品管理接口" },
  { label: "订单数量", value: orders.value.length, desc: "来自订单管理接口" },
  { label: "用户数量", value: users.value.length, desc: "来自用户管理接口" },
  { label: "Banner 数量", value: banners.value.length, desc: "来自 Banner 管理接口" }
]);

async function loadDashboard() {
  const [productPage, orderPage, pendingOrderPage, userPage, bannerPage] = await Promise.all([
    adminApi.getProducts({ pageNum: 1, pageSize: 100 }),
    adminApi.getOrders({ pageNum: 1, pageSize: 100 }),
    adminApi.getOrders({ pageNum: 1, pageSize: 100, status: 1 }),
    adminApi.getUsers({ pageNum: 1, pageSize: 100 }),
    adminApi.getBanners({ pageNum: 1, pageSize: 100 })
  ]);
  products.value = productPage?.list || [];
  orders.value = orderPage?.list || [];
  pendingOrders.value = pendingOrderPage?.list || [];
  users.value = userPage?.list || [];
  banners.value = bannerPage?.list || [];
}

onMounted(loadDashboard);
</script>
