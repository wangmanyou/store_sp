<template>
  <div style="display: grid; gap: 18px">
    <div class="panel" style="display: flex; gap: 12px; align-items: center; padding: 18px; flex-wrap: wrap">
      <span class="section-subtitle">商品名称 / 分类 / 状态管理</span>
      <el-input v-model="query.keyword" placeholder="搜索商品" style="width: 220px; margin-left: auto" clearable @keyup.enter="loadProducts" />
      <el-select v-model="query.categoryId" placeholder="全部分类" style="width: 160px" clearable>
        <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category.id" />
      </el-select>
      <el-button class="brand-danger-button" @click="loadProducts">查询</el-button>
      <button class="btn primary" @click="openCreate">新增商品</button>
    </div>

    <div class="panel" style="padding: 22px">
      <h1 class="section-title">商品列表</h1>
      <el-alert v-if="error" :title="error" type="warning" show-icon :closable="false" style="margin-top: 14px" />
      <el-table v-loading="loading" class="brand-table" :data="products" row-key="id">
        <el-table-column label="商品信息" min-width="260">
          <template #default="{ row }">
            <div style="display: flex; align-items: center; gap: 12px">
              <div class="cart-cover" :style="{ background: coverStyle(row.coverImage) }"></div>
              <div>
                <strong>{{ row.name }}</strong>
                <p class="section-subtitle">{{ row.subtitle }}</p>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="140" />
        <el-table-column label="价格" width="120">
          <template #default="{ row }">¥{{ money(row.price) }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <span class="status" :class="{ success: row.status === 1 }">{{ row.status === 1 ? "上架" : "下架" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="230" fixed="right">
          <template #default="{ row }">
            <el-button link class="brand-link-danger" @click="openEdit(row)">编辑</el-button>
            <el-button link @click="toggleStatus(row)">{{ row.status === 1 ? "下架" : "上架" }}</el-button>
            <el-popconfirm title="确定删除该商品吗？" confirm-button-text="删除" cancel-button-text="取消" @confirm="deleteProduct(row)">
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑商品' : '新增商品'" width="760px" class="brand-dialog">
      <el-form label-width="90px">
        <el-form-item label="商品名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="副标题"><el-input v-model="form.subtitle" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" style="width: 100%">
            <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" /></el-form-item>
        <el-form-item label="封面图">
          <AdminImageUpload v-model="form.coverImage" button-text="上传封面" />
        </el-form-item>
        <el-form-item label="商品图集">
          <ProductImageGalleryUpload v-model="form.images" />
        </el-form-item>
        <el-form-item label="详情"><el-input v-model="form.detail" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">上架</el-radio>
            <el-radio :value="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="brand-dialog-footer">
          <button class="btn secondary" type="button" @click="dialogVisible = false">取消</button>
          <button class="btn primary" type="button" @click="submitProduct">保存</button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import AdminImageUpload from "../../components/AdminImageUpload.vue";
import ProductImageGalleryUpload from "../../components/ProductImageGalleryUpload.vue";
import { adminApi } from "../../services/api";
import { coverStyle, money } from "../../services/format";

const products = ref([]);
const categories = ref([]);
const error = ref("");
const loading = ref(false);
const dialogVisible = ref(false);
const query = reactive({ keyword: "", categoryId: null });
const form = reactive(defaultForm());

function defaultForm() {
  return { id: null, categoryId: null, name: "", subtitle: "", price: 0, stock: 0, coverImage: "", images: "", detail: "", status: 1 };
}

function resetForm(data = {}) {
  Object.assign(form, defaultForm(), data);
}

async function loadCategories() {
  categories.value = await adminApi.getCategories();
}

async function loadProducts() {
  loading.value = true;
  error.value = "";
  try {
    const page = await adminApi.getProducts({ pageNum: 1, pageSize: 50, keyword: query.keyword, categoryId: query.categoryId });
    products.value = page?.list || [];
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
}

function openCreate() {
  resetForm({ categoryId: categories.value[0]?.id || null });
  dialogVisible.value = true;
}

async function openEdit(row) {
  try {
    const detail = await adminApi.getProductDetail(row.id);
    resetForm({ ...detail, images: detail.images || "", detail: detail.detail || "" });
    dialogVisible.value = true;
  } catch (err) {
    ElMessage.error(err.message || "商品详情加载失败");
  }
}

async function submitProduct() {
  try {
    if (form.id) {
      await adminApi.updateProduct(form);
    } else {
      await adminApi.saveProduct(form);
    }
    ElMessage.success("保存成功");
    dialogVisible.value = false;
    await loadProducts();
  } catch (err) {
    ElMessage.error(err.message || "保存失败");
  }
}

async function toggleStatus(row) {
  await adminApi.updateProductStatus(row.id, row.status === 1 ? 0 : 1);
  ElMessage.success("状态已更新");
  await loadProducts();
}

async function deleteProduct(row) {
  await adminApi.deleteProduct(row.id);
  ElMessage.success("删除成功");
  await loadProducts();
}

onMounted(async () => {
  await loadCategories();
  await loadProducts();
});
</script>
