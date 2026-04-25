<template>
  <div style="display: grid; gap: 18px">
    <div class="panel admin-toolbar">
      <span class="section-subtitle">Banner 标题、展示顺序与上下线管理</span>
      <button class="btn primary" style="margin-left: auto" @click="openCreate">新增 Banner</button>
    </div>

    <el-alert v-if="error" :title="error" type="warning" show-icon :closable="false" />
    <el-empty v-if="!banners.length && !error" description="暂无 Banner 数据" />

    <div v-else class="grid cols-3">
      <article v-for="banner in banners" :key="banner.id" class="panel banner-card">
        <div class="hero-card banner-cover" :style="{ background: coverStyle(banner.imageUrl) }"></div>
        <h2 class="brand-title" style="font-size: 34px; margin: 18px 0 8px">{{ banner.title }}</h2>
        <p class="section-subtitle">{{ banner.subtitle || "暂无副标题" }}</p>
        <div style="display: grid; gap: 10px; margin-top: 16px">
          <div class="banner-meta">
            <span class="section-subtitle">状态</span>
            <strong class="status" :class="{ success: banner.status === 1 }">
              {{ banner.status === 1 ? "启用" : "禁用" }}
            </strong>
          </div>
          <div class="banner-meta">
            <span class="section-subtitle">排序</span>
            <strong>{{ banner.sort }}</strong>
          </div>
        </div>
        <div class="banner-actions">
          <button class="btn secondary action-button" @click="openEdit(banner)">编辑</button>
          <button class="btn primary action-button" @click="toggleStatus(banner)">
            {{ banner.status === 1 ? "下线" : "上线" }}
          </button>
          <el-popconfirm title="确定删除该 Banner 吗？" confirm-button-text="删除" cancel-button-text="取消" @confirm="deleteBanner(banner)">
            <template #reference>
              <button class="btn danger action-button">删除</button>
            </template>
          </el-popconfirm>
        </div>
      </article>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑 Banner' : '新增 Banner'" width="620px" class="brand-dialog">
      <el-form label-width="90px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="副标题"><el-input v-model="form.subtitle" /></el-form-item>
        <el-form-item label="图片">
          <AdminImageUpload v-model="form.imageUrl" button-text="上传 Banner" />
        </el-form-item>
        <el-form-item label="跳转地址"><el-input v-model="form.linkUrl" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="brand-dialog-footer">
          <button class="btn secondary" type="button" @click="dialogVisible = false">取消</button>
          <button class="btn primary" type="button" @click="submitBanner">保存</button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import AdminImageUpload from "../../components/AdminImageUpload.vue";
import { adminApi } from "../../services/api";
import { coverStyle } from "../../services/format";

const banners = ref([]);
const error = ref("");
const dialogVisible = ref(false);
const form = reactive({ id: null, title: "", subtitle: "", imageUrl: "", linkUrl: "", sort: 0, status: 1 });

function resetForm(data = {}) {
  Object.assign(form, { id: null, title: "", subtitle: "", imageUrl: "", linkUrl: "", sort: 0, status: 1 }, data);
}

async function loadBanners() {
  try {
    const page = await adminApi.getBanners({ pageNum: 1, pageSize: 20 });
    banners.value = page?.list || [];
  } catch (err) {
    error.value = err.message;
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

async function submitBanner() {
  try {
    if (form.id) {
      await adminApi.updateBanner(form);
    } else {
      await adminApi.saveBanner(form);
    }
    ElMessage.success("保存成功");
    dialogVisible.value = false;
    await loadBanners();
  } catch (err) {
    ElMessage.error(err.message || "保存失败");
  }
}

async function toggleStatus(row) {
  await adminApi.updateBannerStatus(row.id, row.status === 1 ? 0 : 1);
  ElMessage.success("状态已更新");
  await loadBanners();
}

async function deleteBanner(row) {
  await adminApi.deleteBanner(row.id);
  ElMessage.success("删除成功");
  await loadBanners();
}

onMounted(loadBanners);
</script>
