<template>
  <div class="cart-shell">
    <section class="panel cart-panel">
      <div class="cart-toolbar">
        <div>
          <h1 class="section-title">购物车</h1>
          <p class="section-subtitle" style="margin-top: 8px">选择商品、调整数量，然后提交订单。</p>
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
        <el-button class="brand-checkout" size="large" :disabled="selectedItems.length === 0" @click="openCheckout">
          提交订单
        </el-button>
        <RouterLink class="btn primary" to="/products">继续购物</RouterLink>
      </div>
    </aside>
  </div>

  <el-dialog v-model="checkoutVisible" class="brand-dialog" title="确认订单" width="720px">
    <div v-loading="checkoutLoading" class="checkout-dialog">
      <div class="checkout-block">
        <h3>收货地址</h3>
        <el-radio-group v-if="addresses.length" v-model="checkoutForm.addressId" class="address-list">
          <el-radio v-for="address in addresses" :key="address.id" :value="address.id" class="address-card">
            <strong>{{ address.receiverName }} {{ address.receiverPhone }}</strong>
            <span>{{ fullAddress(address) }}</span>
          </el-radio>
        </el-radio-group>

        <el-alert
          v-else
          title="还没有收货地址，请先填写一个地址。"
          type="warning"
          show-icon
          :closable="false"
        />

        <el-button class="brand-save-button" style="margin-top: 14px" @click="showAddressForm = !showAddressForm">
          {{ showAddressForm ? "收起地址表单" : "新增收货地址" }}
        </el-button>
      </div>

      <el-form v-if="showAddressForm || !addresses.length" label-width="86px" class="checkout-address-form">
        <el-form-item label="收货人">
          <el-input v-model="addressForm.receiverName" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="addressForm.receiverPhone" placeholder="请输入手机号" />
        </el-form-item>
        <div class="checkout-address-grid">
          <el-form-item label="省份">
            <el-input v-model="addressForm.province" placeholder="省份" />
          </el-form-item>
          <el-form-item label="城市">
            <el-input v-model="addressForm.city" placeholder="城市" />
          </el-form-item>
          <el-form-item label="区县">
            <el-input v-model="addressForm.district" placeholder="区县" />
          </el-form-item>
        </div>
        <el-form-item label="详细地址">
          <el-input v-model="addressForm.detailAddress" placeholder="街道、门牌号等" />
        </el-form-item>
        <el-form-item>
          <el-button class="brand-save-button" @click="saveAddress">保存地址</el-button>
        </el-form-item>
      </el-form>

      <div class="checkout-block">
        <h3>订单备注</h3>
        <el-input v-model="checkoutForm.remark" type="textarea" :rows="3" placeholder="可填写配送备注，可不填" />
      </div>
    </div>
    <template #footer>
      <div class="brand-dialog-footer">
        <el-button @click="checkoutVisible = false">取消</el-button>
        <el-button class="brand-save-button" :loading="submitLoading" @click="submitOrder">确认提交</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { addressApi, cartApi, orderApi } from "../../services/api";
import { coverStyle, money } from "../../services/format";

const cartItems = ref([]);
const selectedItems = ref([]);
const addresses = ref([]);
const loading = ref(false);
const checkoutLoading = ref(false);
const submitLoading = ref(false);
const checkoutVisible = ref(false);
const showAddressForm = ref(false);
const error = ref("");

const checkoutForm = reactive({
  addressId: null,
  remark: ""
});

const addressForm = reactive({
  receiverName: "",
  receiverPhone: "",
  province: "",
  city: "",
  district: "",
  detailAddress: "",
  isDefault: 1
});

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
    error.value = err.message || "请先登录后查看购物车";
  } finally {
    loading.value = false;
  }
}

async function loadAddresses() {
  addresses.value = await addressApi.getAddresses();
  if (!checkoutForm.addressId && addresses.value.length) {
    const defaultAddress = addresses.value.find((item) => item.isDefault === 1);
    checkoutForm.addressId = defaultAddress?.id || addresses.value[0].id;
  }
}

async function openCheckout() {
  checkoutVisible.value = true;
  checkoutLoading.value = true;
  try {
    await loadAddresses();
    showAddressForm.value = addresses.value.length === 0;
  } catch (err) {
    ElMessage.error(err.message || "收货地址加载失败");
  } finally {
    checkoutLoading.value = false;
  }
}

async function saveAddress() {
  if (!addressForm.receiverName || !addressForm.receiverPhone || !addressForm.province || !addressForm.city || !addressForm.district || !addressForm.detailAddress) {
    ElMessage.warning("请把收货地址填写完整");
    return;
  }
  try {
    await addressApi.saveAddress(addressForm);
    ElMessage.success("地址已保存");
    showAddressForm.value = false;
    await loadAddresses();
  } catch (err) {
    ElMessage.error(err.message || "地址保存失败");
  }
}

async function submitOrder() {
  if (!checkoutForm.addressId) {
    ElMessage.warning("请选择收货地址");
    return;
  }
  submitLoading.value = true;
  try {
    await syncCheckedItems();
    await orderApi.submitOrder({
      addressId: checkoutForm.addressId,
      remark: checkoutForm.remark
    });
    ElMessage.success("下单成功");
    checkoutVisible.value = false;
    selectedItems.value = [];
    await loadCart();
  } catch (err) {
    ElMessage.error(err.message || "下单失败");
  } finally {
    submitLoading.value = false;
  }
}

async function syncCheckedItems() {
  const selectedIds = new Set(selectedItems.value.map((item) => item.cartId));
  await Promise.all(
    cartItems.value.map((item) =>
      cartApi.updateCart({
        cartId: item.cartId,
        quantity: item.quantity,
        checked: selectedIds.has(item.cartId) ? 1 : 0
      })
    )
  );
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

function fullAddress(address) {
  return `${address.province || ""}${address.city || ""}${address.district || ""}${address.detailAddress || ""}`;
}

onMounted(loadCart);
</script>
