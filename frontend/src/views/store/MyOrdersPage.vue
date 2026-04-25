<template>
  <div class="account-page">
    <section class="panel account-hero">
      <div>
        <span class="chip">Orders</span>
        <h1 class="section-title">我的购物</h1>
        <p class="section-subtitle">查看已提交的订单、收货信息和物流状态。</p>
      </div>
      <RouterLink class="btn primary" to="/products">继续购物</RouterLink>
    </section>

    <section class="panel account-panel">
      <div class="order-filter">
        <button
          v-for="item in statusTabs"
          :key="item.value"
          class="chip"
          :class="{ active: activeStatus === item.value }"
          type="button"
          @click="changeStatus(item.value)"
        >
          {{ item.label }}
        </button>
      </div>

      <el-empty v-if="!loading && orders.length === 0" description="暂无订单" />

      <div v-else v-loading="loading" class="my-order-list">
        <article v-for="order in orders" :key="order.id" class="my-order-card">
          <div class="my-order-head">
            <div>
              <strong>订单号 {{ order.orderNo }}</strong>
              <span>{{ order.createTime }}</span>
            </div>
            <em>{{ orderStatusText(order.status) }}</em>
          </div>
          <div class="my-order-body">
            <div>
              <span>收货人</span>
              <strong>{{ order.receiverName }} {{ order.receiverPhone }}</strong>
            </div>
            <div>
              <span>收货地址</span>
              <strong>{{ order.receiverAddress }}</strong>
            </div>
            <div>
              <span>订单金额</span>
              <strong>￥{{ money(order.totalAmount) }}</strong>
            </div>
          </div>
          <div class="my-order-actions">
            <button class="btn secondary" type="button" @click="openDetail(order.id)">查看详情</button>
          </div>
        </article>
      </div>

      <el-pagination
        v-if="total > pageSize"
        class="account-pagination"
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        v-model:current-page="pageNum"
        @current-change="loadOrders"
      />
    </section>
  </div>

  <el-dialog v-model="detailVisible" class="brand-dialog" title="订单详情" width="760px">
    <div v-if="orderDetail" class="order-detail-view">
      <div class="order-detail-summary">
        <div><span>订单号</span><strong>{{ orderDetail.orderNo }}</strong></div>
        <div><span>状态</span><strong>{{ orderStatusText(orderDetail.status) }}</strong></div>
        <div><span>金额</span><strong>￥{{ money(orderDetail.totalAmount) }}</strong></div>
      </div>

      <div class="order-detail-address">
        <strong>{{ orderDetail.receiverName }} {{ orderDetail.receiverPhone }}</strong>
        <span>{{ orderDetail.receiverAddress }}</span>
        <span v-if="orderDetail.expressCompany">物流：{{ orderDetail.expressCompany }} {{ orderDetail.expressNo }}</span>
      </div>

      <div class="order-detail-items">
        <div v-for="item in orderDetail.items || []" :key="item.id" class="order-detail-item">
          <div class="order-item-cover" :style="{ background: coverStyle(item.productImage) }"></div>
          <div>
            <strong>{{ item.productName }}</strong>
            <span>￥{{ money(item.productPrice) }} × {{ item.quantity }}</span>
          </div>
          <em>￥{{ money(item.subtotal) }}</em>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import { orderApi } from "../../services/api";
import { coverStyle, money, orderStatusText } from "../../services/format";

const statusTabs = [
  { label: "全部", value: null },
  { label: "待发货", value: 1 },
  { label: "已发货", value: 2 },
  { label: "已完成", value: 3 },
  { label: "已取消", value: 4 }
];

const orders = ref([]);
const orderDetail = ref(null);
const loading = ref(false);
const detailVisible = ref(false);
const activeStatus = ref(null);
const pageNum = ref(1);
const pageSize = 8;
const total = ref(0);

async function loadOrders() {
  loading.value = true;
  try {
    const result = await orderApi.getOrders({
      pageNum: pageNum.value,
      pageSize,
      status: activeStatus.value
    });
    orders.value = result.records || result.list || [];
    total.value = Number(result.total || 0);
  } catch (err) {
    ElMessage.error(err.message || "订单加载失败");
  } finally {
    loading.value = false;
  }
}

function changeStatus(status) {
  activeStatus.value = status;
  pageNum.value = 1;
  loadOrders();
}

async function openDetail(id) {
  try {
    orderDetail.value = await orderApi.getOrderDetail(id);
    detailVisible.value = true;
  } catch (err) {
    ElMessage.error(err.message || "订单详情加载失败");
  }
}

onMounted(loadOrders);
</script>
