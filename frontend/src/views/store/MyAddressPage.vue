<template>
  <div class="account-page">
    <section class="panel account-hero">
      <div>
        <span class="chip">Account</span>
        <h1 class="section-title">我的地址</h1>
        <p class="section-subtitle">管理收货人、手机号和配送地址，下单时可以直接选择。</p>
      </div>
      <button class="btn primary" type="button" @click="openCreate">新增地址</button>
    </section>

    <section class="panel account-panel">
      <el-empty v-if="!loading && addresses.length === 0" description="暂无收货地址" />

      <div v-else v-loading="loading" class="address-grid-list">
        <article v-for="address in addresses" :key="address.id" class="user-address-card">
          <div class="user-address-head">
            <strong>{{ address.receiverName }}</strong>
            <span>{{ address.receiverPhone }}</span>
            <em v-if="address.isDefault === 1">默认</em>
          </div>
          <p>{{ fullAddress(address) }}</p>
          <div class="user-address-actions">
            <button class="btn secondary" type="button" @click="openEdit(address)">编辑</button>
            <button v-if="address.isDefault !== 1" class="btn secondary" type="button" @click="setDefault(address.id)">设为默认</button>
            <el-popconfirm title="确定删除该地址吗？" confirm-button-text="删除" cancel-button-text="取消" @confirm="deleteAddress(address.id)">
              <template #reference>
                <button class="btn danger" type="button">删除</button>
              </template>
            </el-popconfirm>
          </div>
        </article>
      </div>
    </section>
  </div>

  <el-dialog v-model="dialogVisible" class="brand-dialog" :title="form.id ? '编辑地址' : '新增地址'" width="640px">
    <el-form label-width="86px">
      <el-form-item label="收货人">
        <el-input v-model="form.receiverName" placeholder="请输入收货人姓名" />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="form.receiverPhone" placeholder="请输入手机号" />
      </el-form-item>
      <div class="checkout-address-grid">
        <el-form-item label="省份">
          <el-input v-model="form.province" placeholder="省份" />
        </el-form-item>
        <el-form-item label="城市">
          <el-input v-model="form.city" placeholder="城市" />
        </el-form-item>
        <el-form-item label="区县">
          <el-input v-model="form.district" placeholder="区县" />
        </el-form-item>
      </div>
      <el-form-item label="详细地址">
        <el-input v-model="form.detailAddress" placeholder="街道、门牌号等" />
      </el-form-item>
      <el-form-item>
        <el-checkbox v-model="isDefault">设为默认地址</el-checkbox>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="brand-dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button class="brand-save-button" :loading="saving" @click="saveAddress">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { addressApi } from "../../services/api";

const addresses = ref([]);
const loading = ref(false);
const saving = ref(false);
const dialogVisible = ref(false);
const isDefault = ref(false);

const emptyForm = {
  id: null,
  receiverName: "",
  receiverPhone: "",
  province: "",
  city: "",
  district: "",
  detailAddress: ""
};

const form = reactive({ ...emptyForm });

async function loadAddresses() {
  loading.value = true;
  try {
    addresses.value = await addressApi.getAddresses();
  } catch (err) {
    ElMessage.error(err.message || "地址加载失败");
  } finally {
    loading.value = false;
  }
}

function openCreate() {
  Object.assign(form, emptyForm);
  isDefault.value = addresses.value.length === 0;
  dialogVisible.value = true;
}

function openEdit(address) {
  Object.assign(form, address);
  isDefault.value = address.isDefault === 1;
  dialogVisible.value = true;
}

async function saveAddress() {
  if (!form.receiverName || !form.receiverPhone || !form.province || !form.city || !form.district || !form.detailAddress) {
    ElMessage.warning("请把地址填写完整");
    return;
  }
  saving.value = true;
  try {
    const data = { ...form, isDefault: isDefault.value ? 1 : 0 };
    if (form.id) {
      await addressApi.updateAddress(data);
    } else {
      await addressApi.saveAddress(data);
    }
    ElMessage.success("地址已保存");
    dialogVisible.value = false;
    await loadAddresses();
  } catch (err) {
    ElMessage.error(err.message || "保存失败");
  } finally {
    saving.value = false;
  }
}

async function setDefault(id) {
  try {
    await addressApi.setDefault(id);
    ElMessage.success("默认地址已更新");
    await loadAddresses();
  } catch (err) {
    ElMessage.error(err.message || "设置失败");
  }
}

async function deleteAddress(id) {
  try {
    await addressApi.deleteAddress(id);
    ElMessage.success("地址已删除");
    await loadAddresses();
  } catch (err) {
    ElMessage.error(err.message || "删除失败");
  }
}

function fullAddress(address) {
  return `${address.province || ""}${address.city || ""}${address.district || ""}${address.detailAddress || ""}`;
}

onMounted(loadAddresses);
</script>
