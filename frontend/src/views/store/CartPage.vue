<template>
  <div class="cart-shell">
    <section class="panel cart-panel">
      <div class="cart-toolbar">
        <div>
          <h1 class="section-title">Your bag</h1>
          <p class="section-subtitle" style="margin-top: 8px">支持多选、数量编辑、单个删除和批量删除。</p>
        </div>
        <el-popconfirm title="确定删除选中的商品吗？" confirm-button-text="删除" cancel-button-text="取消" @confirm="batchDelete">
          <template #reference>
            <el-button class="brand-danger-button" :disabled="selectedItems.length === 0">批量删除</el-button>
          </template>
        </el-popconfirm>
      </div>

      <el-alert v-if="error" :title="error" type="warning" show-icon :closable="false" style="margin-top: 18px" />

      <el-table
        v-loading="loading"
        class="brand-table"
        :data="cartItems"
        row-key="cartId"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="52" />
        <el-table-column label="商品信息" min-width="280">
          <template #default="{ row }">
            <div class="cart-product">
              <div class="cart-cover" :style="{ background: coverStyle(row.coverImage) }"></div>
              <div>
                <div class="brand-title cart-product-name">{{ row.productName }}</div>
                <div class="section-subtitle">库存 {{ row.stock }} · {{ row.status === 1 ? "销售中" : "已下架" }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="单价" width="120">
          <template #default="{ row }">￥{{ money(row.price) }}</template>
        </el-table-column>
        <el-table-column label="数量" width="180">
          <template #default="{ row }">
            <el-input-number
              v-model="row.quantity"
              class="brand-number"
              :min="1"
              :max="row.stock || 999"
              size="small"
              @change="(value) => updateQuantity(row, value)"
            />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="130">
          <template #default="{ row }">
            <strong>￥{{ money(row.price * row.quantity) }}</strong>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-popconfirm title="确定删除该商品吗？" confirm-button-text="删除" cancel-button-text="取消" @confirm="deleteItem(row)">
              <template #reference>
                <el-button link class="brand-link-danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <aside class="panel cart-summary">
      <h2 class="brand-title" style="font-size: 38px; margin: 0 0 16px">Summary</h2>
      <div style="display: grid; gap: 14px">
        <div class="summary-row"><span>已选商品</span><strong>{{ selectedItems.length }} 件</strong></div>
        <div class="summary-row"><span>Subtotal</span><strong>￥{{ money(selectedTotal) }}</strong></div>
        <div class="summary-row"><span>Delivery</span><strong>Free</strong></div>
        <div class="summary-row total"><span>Total</span><strong>￥{{ money(selectedTotal) }}</strong></div>
      </div>
      <div style="display: grid; gap: 12px; margin-top: 22px">
        <el-button class="brand-checkout" size="large" :disabled="selectedItems.length === 0">提交订单</el-button>
        <RouterLink class="btn primary" to="/products">继续购物</RouterLink>
      </div>
    </aside>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import { cartApi } from "../../services/api";
import { coverStyle, money } from "../../services/format";

const cartItems = ref([]);
const selectedItems = ref([]);
const loading = ref(false);
const error = ref("");

const selectedTotal = computed(() =>
  selectedItems.value.reduce((sum, item) => sum + Number(item.price || 0) * Number(item.quantity || 0), 0)
);

function handleSelectionChange(rows) {
  selectedItems.value = rows;
}

async function loadCart() {
  loading.value = true;
  error.value = "";
  try {
    cartItems.value = await cartApi.getCart();
  } catch (err) {
    error.value = err.message || "请先登录后查看购物袋";
  } finally {
    loading.value = false;
  }
}

async function updateQuantity(row, quantity) {
  try {
    await cartApi.updateCart({
      cartId: row.cartId,
      quantity,
      checked: row.checked
    });
    row.subtotal = Number(row.price || 0) * Number(quantity || 0);
    ElMessage.success("数量已更新");
  } catch (err) {
    ElMessage.error(err.message || "数量更新失败");
    await loadCart();
  }
}

async function deleteItem(row) {
  try {
    await cartApi.deleteCart(row.cartId);
    ElMessage.success("删除成功");
    await loadCart();
  } catch (err) {
    ElMessage.error(err.message || "删除失败");
  }
}

async function batchDelete() {
  try {
    await Promise.all(selectedItems.value.map((item) => cartApi.deleteCart(item.cartId)));
    ElMessage.success("批量删除成功");
    selectedItems.value = [];
    await loadCart();
  } catch (err) {
    ElMessage.error(err.message || "批量删除失败");
  }
}

onMounted(loadCart);
</script>
