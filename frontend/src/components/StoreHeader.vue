<template>
  <header class="container" style="padding: 24px 0 16px">
    <div class="panel store-header-panel">
      <BrandLogo />
      <nav class="store-nav">
        <RouterLink to="/">首页精选</RouterLink>
        <RouterLink to="/products">商品列表</RouterLink>
        <RouterLink to="/my-orders">我的购物</RouterLink>
        <RouterLink to="/my-address">我的地址</RouterLink>
      </nav>
      <div class="store-header-actions">
        <span class="chip">搜索</span>
        <RouterLink v-if="isCartPage" class="chip active" to="/products">继续购物</RouterLink>
        <RouterLink v-else class="chip" to="/cart">购物车</RouterLink>

        <el-dropdown v-if="isLoggedIn" trigger="hover" placement="bottom-end" popper-class="brand-dropdown" @command="handleUserCommand">
          <button class="btn primary user-trigger">
            {{ displayName }}
          </button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="address">我的地址</el-dropdown-item>
              <el-dropdown-item command="orders">我的购物</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

        <RouterLink v-else class="btn primary" to="/login">登录</RouterLink>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import BrandLogo from "./BrandLogo.vue";
import { AUTH_CHANGED_EVENT, clearToken, getUserSession } from "../services/request";

const router = useRouter();
const route = useRoute();
const userInfo = ref(null);

const isLoggedIn = computed(() => Boolean(userInfo.value));
const isCartPage = computed(() => route.path === "/cart");
const displayName = computed(() => userInfo.value?.nickname || userInfo.value?.username || "我的账户");

function refreshSession() {
  const session = getUserSession();
  userInfo.value = session.token ? session.userInfo : null;
}

function handleUserCommand(command) {
  if (command === "address") {
    router.push("/my-address");
    return;
  }
  if (command === "orders") {
    router.push("/my-orders");
    return;
  }
  if (command === "logout") {
    clearToken("USER");
    userInfo.value = null;
    router.push("/login");
  }
}

onMounted(() => {
  refreshSession();
  window.addEventListener(AUTH_CHANGED_EVENT, refreshSession);
  window.addEventListener("storage", refreshSession);
});

onUnmounted(() => {
  window.removeEventListener(AUTH_CHANGED_EVENT, refreshSession);
  window.removeEventListener("storage", refreshSession);
});
</script>
