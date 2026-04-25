<template>
  <div style="display: grid; gap: 18px">
    <div class="panel" style="display: flex; gap: 12px; align-items: center; padding: 18px">
      <span class="section-subtitle">订单号 / 用户 / 发货状态管理</span>
      <el-input v-model="query.orderNo" placeholder="搜索订单号" style="width: 220px; margin-left: auto" clearable @keyup.enter="loadOrders" />
      <el-button class="brand-danger-button" @click="loadOrders">查询</el-button>
    </div>

    <el-alert v-if="error" :title="error" type="warning" show-icon :closable="false" />
    <div style="display: grid; gap: 14px">
      <article v-for="order in orders" :key="order.id" class="panel" style="padding: 22px">
        <div style="display: flex; align-items: center; gap: 12px; flex-wrap: wrap">
          <strong>订单号：{{ order.orderNo }}</strong>
          <span class="chip" :class="{ active: order.status === 1 }">{{ orderStatusText(order.status) }}</span>
        </div>
        <div style="display: flex; gap: 24px; color: var(--muted); margin-top: 14px; flex-wrap: wrap">
          <span>用户：{{ order.username }}</span>
          <span>金额：¥{{ money(order.totalAmount) }}</span>
          <span>地址：{{ order.receiverAddress }}</span>
        </div>
        <div style="display: flex; gap: 12px; margin-top: 16px">
          <button class="btn secondary" @click="openDetail(order)">查看详情</button>
          <button class="btn primary" :disabled="order.status !== 1" @click="openDeliver(order)">
            {{ order.status === 1 ? "立即发货" : "已处理" }}
          </button>
        </div>
      </article>
    </div>

    <el-dialog v-model="deliverVisible" title="订单发货" width="460px" class="brand-dialog">
      <el-form label-width="90px">
        <el-form-item label="快递公司"><el-input v-model="deliverForm.expressCompany" /></el-form-item>
        <el-form-item label="快递单号"><el-input v-model="deliverForm.expressNo" /></el-form-item>
      </el-form>
      <template #footer>
        <div class="brand-dialog-footer">
          <button class="btn secondary" type="button" @click="deliverVisible = false">取消</button>
          <button class="btn primary" type="button" @click="submitDeliver">确认发货</button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="订单详情" width="680px" class="brand-dialog">
      <div v-if="currentDetail" style="display: grid; gap: 14px">
        <div class="soft-panel" style="padding: 16px; display: grid; gap: 8px">
          <strong>{{ currentDetail.orderNo }}</strong>
          <span class="section-subtitle">收货人：{{ currentDetail.receiverName }} / {{ currentDetail.receiverPhone }}</span>
          <span class="section-subtitle">地址：{{ currentDetail.receiverAddress }}</span>
          <span v-if="currentDetail.expressNo" class="section-subtitle">
            物流：{{ currentDetail.expressCompany }} {{ currentDetail.expressNo }}
          </span>
        </div>
        <div v-for="item in currentDetail.items || []" :key="item.id" class="cart-product">
          <div class="cart-cover" :style="{ background: coverStyle(item.productImage) }"></div>
          <div style="flex: 1">
            <strong>{{ item.productName }}</strong>
            <p class="section-subtitle">数量：{{ item.quantity }} / 小计：¥{{ money(item.subtotal) }}</p>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { adminApi } from "../../services/api";
import { coverStyle, money, orderStatusText } from "../../services/format";

const orders = ref([]);
const error = ref("");
const deliverVisible = ref(false);
const detailVisible = ref(false);
const currentDetail = ref(null);
const query = reactive({ orderNo: "" });
const deliverForm = reactive({ orderId: null, expressCompany: "", expressNo: "" });

async function loadOrders() {
  try {
    const page = await adminApi.getOrders({ pageNum: 1, pageSize: 20, orderNo: query.orderNo });
    orders.value = page?.list || [];
  } catch (err) {
    error.value = err.message;
  }
}

function openDeliver(order) {
  Object.assign(deliverForm, { orderId: order.id, expressCompany: "", expressNo: "" });
  deliverVisible.value = true;
}

async function openDetail(order) {
  currentDetail.value = await adminApi.getOrderDetail(order.id);
  detailVisible.value = true;
}

async function submitDeliver() {
  try {
    await adminApi.deliverOrder(deliverForm);
    ElMessage.success("发货成功");
    deliverVisible.value = false;
    await loadOrders();
  } catch (err) {
    ElMessage.error(err.message || "发货失败");
  }
}

onMounted(loadOrders);
</script>
