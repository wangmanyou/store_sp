<template>
  <header class="container" style="padding: 24px 0 16px">
    <div
      class="panel"
      style="display: flex; align-items: center; gap: 24px; padding: 22px 28px; border-radius: 28px"
    >
      <RouterLink class="brand-title" to="/" style="font-size: 34px; font-weight: 700">
        Aureline
      </RouterLink>
      <nav style="display: flex; gap: 18px; color: var(--muted); font-size: 14px; font-weight: 500">
        <RouterLink to="/">新季精选</RouterLink>
        <RouterLink to="/products">商品列表</RouterLink>
        <a href="#">配饰</a>
        <a href="#">家居美学</a>
      </nav>
      <div style="margin-left: auto; display: flex; gap: 12px; align-items: center">
        <span class="chip">搜索</span>
        <RouterLink v-if="isCartPage" class="chip active" to="/products">继续购物</RouterLink>
        <RouterLink v-else class="chip" to="/cart">购物袋</RouterLink>

        <el-dropdown v-if="isLoggedIn" trigger="hover" placement="bottom-end" popper-class="brand-dropdown" @command="handleUserCommand">
          <button class="btn primary user-trigger">
            {{ displayName }}
          </button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="cart">我的购物袋</el-dropdown-item>
              <el-dropdown-item command="products">继续购物</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
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
import { useRoute } from "vue-router";
import { useRouter } from "vue-router";
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
  if (command === "cart") {
    router.push("/cart");
    return;
  }
  if (command === "products") {
    router.push("/products");
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
