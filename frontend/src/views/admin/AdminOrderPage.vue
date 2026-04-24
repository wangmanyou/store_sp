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
        <div style="display: flex; align-items: center; gap: 12px">
          <strong>订单号：{{ order.orderNo }}</strong>
          <span class="chip" :class="{ active: order.status === 1 }">{{ orderStatusText(order.status) }}</span>
        </div>
        <div style="display: flex; gap: 24px; color: var(--muted); margin-top: 14px; flex-wrap: wrap">
          <span>用户：{{ order.username }}</span>
          <span>金额：￥{{ money(order.totalAmount) }}</span>
          <span>地址：{{ order.receiverAddress }}</span>
        </div>
        <div style="display: flex; gap: 12px; margin-top: 16px">
          <button class="btn secondary">查看详情</button>
          <button class="btn primary" :disabled="order.status !== 1" @click="openDeliver(order)">
            {{ order.status === 1 ? "立即发货" : "已处理" }}
          </button>
        </div>
      </article>
    </div>

    <el-dialog v-model="dialogVisible" title="订单发货" width="420px">
      <el-form label-width="90px">
        <el-form-item label="快递公司"><el-input v-model="deliverForm.expressCompany" /></el-form-item>
        <el-form-item label="快递单号"><el-input v-model="deliverForm.expressNo" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button class="brand-checkout" @click="submitDeliver">确认发货</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { adminApi } from "../../services/api";
import { money, orderStatusText } from "../../services/format";

const orders = ref([]);
const error = ref("");
const dialogVisible = ref(false);
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
  dialogVisible.value = true;
}

async function submitDeliver() {
  try {
    await adminApi.deliverOrder(deliverForm);
    ElMessage.success("发货成功");
    dialogVisible.value = false;
    await loadOrders();
  } catch (err) {
    ElMessage.error(err.message || "发货失败");
  }
}

onMounted(loadOrders);
</script>
