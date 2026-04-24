<template>
  <div style="display: grid; gap: 18px">
    <div class="panel" style="display: flex; gap: 12px; align-items: center; padding: 18px">
      <span class="section-subtitle">管理商品分类、排序和展示状态</span>
      <button class="btn primary" style="margin-left: auto" @click="openCreate">新增分类</button>
    </div>

    <div class="panel" style="padding: 22px">
      <h1 class="section-title">分类列表</h1>
      <el-alert v-if="error" :title="error" type="warning" show-icon :closable="false" style="margin-top: 14px" />
      <el-table v-loading="loading" class="brand-table" :data="categories" row-key="id">
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="sort" label="排序" width="120" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <span class="status" :class="{ success: row.status === 1 }">{{ row.status === 1 ? "启用" : "禁用" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button link class="brand-link-danger" @click="openEdit(row)">编辑</el-button>
            <el-button link @click="toggleStatus(row)">{{ row.status === 1 ? "禁用" : "启用" }}</el-button>
            <el-popconfirm title="确定删除该分类吗？" confirm-button-text="删除" cancel-button-text="取消" @confirm="deleteCategory(row)">
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑分类' : '新增分类'" width="420px">
      <el-form label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button class="brand-checkout" @click="submitCategory">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { adminApi } from "../../services/api";

const categories = ref([]);
const error = ref("");
const loading = ref(false);
const dialogVisible = ref(false);
const form = reactive({ id: null, name: "", sort: 0, status: 1 });

function resetForm(data = {}) {
  Object.assign(form, { id: null, name: "", sort: 0, status: 1 }, data);
}

async function loadCategories() {
  loading.value = true;
  error.value = "";
  try {
    categories.value = await adminApi.getCategories();
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
}

function openCreate() {
  resetForm();
  dialogVisible.value = true;
}

function openEdit(row) {
  resetForm(row);
  dialogVisible.value = true;
}

async function submitCategory() {
  try {
    if (form.id) {
      await adminApi.updateCategory(form);
    } else {
      await adminApi.saveCategory(form);
    }
    ElMessage.success("保存成功");
    dialogVisible.value = false;
    await loadCategories();
  } catch (err) {
    ElMessage.error(err.message || "保存失败");
  }
}

async function toggleStatus(row) {
  await adminApi.updateCategory({ ...row, status: row.status === 1 ? 0 : 1 });
  ElMessage.success("状态已更新");
  await loadCategories();
}

async function deleteCategory(row) {
  await adminApi.deleteCategory(row.id);
  ElMessage.success("删除成功");
  await loadCategories();
}

onMounted(loadCategories);
</script>
