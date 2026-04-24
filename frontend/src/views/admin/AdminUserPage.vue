<template>
  <div style="display: grid; gap: 18px">
    <div class="panel" style="display: flex; gap: 12px; align-items: center; padding: 18px">
      <span class="section-subtitle">用户账号、手机号和状态管理</span>
      <el-input v-model="query.keyword" placeholder="搜索用户" style="width: 220px; margin-left: auto" clearable @keyup.enter="loadUsers" />
      <el-button class="brand-danger-button" @click="loadUsers">查询</el-button>
    </div>

    <div class="panel" style="padding: 22px">
      <h1 class="section-title">用户列表</h1>
      <el-alert v-if="error" :title="error" type="warning" show-icon :closable="false" style="margin-top: 14px" />
      <el-table v-loading="loading" class="brand-table" :data="users" row-key="id">
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <span class="status" :class="{ success: row.status === 1 }">{{ row.status === 1 ? "启用" : "禁用" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-popconfirm :title="`确定${row.status === 1 ? '禁用' : '启用'}该用户吗？`" confirm-button-text="确定" cancel-button-text="取消" @confirm="toggleStatus(row)">
              <template #reference>
                <el-button link class="brand-link-danger">{{ row.status === 1 ? "禁用" : "启用" }}</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { adminApi } from "../../services/api";

const users = ref([]);
const error = ref("");
const loading = ref(false);
const query = reactive({ keyword: "" });

async function loadUsers() {
  loading.value = true;
  error.value = "";
  try {
    const page = await adminApi.getUsers({ pageNum: 1, pageSize: 20, keyword: query.keyword });
    users.value = page?.list || [];
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
}

async function toggleStatus(row) {
  try {
    await adminApi.updateUserStatus(row.id, row.status === 1 ? 0 : 1);
    ElMessage.success("状态已更新");
    await loadUsers();
  } catch (err) {
    ElMessage.error(err.message || "操作失败");
  }
}

onMounted(loadUsers);
</script>
