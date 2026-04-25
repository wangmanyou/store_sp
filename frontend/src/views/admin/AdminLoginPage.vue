<template>
  <div class="container" style="padding: 24px 0">
    <div class="grid cols-2">
      <section class="panel" style="padding: 34px; min-height: 620px; background: #1b1714; color: #fff7ee">
        <span class="chip" style="width: fit-content; background: #2b241f; border: none; color: #e8d7c8">
          BACK OFFICE ACCESS
        </span>
        <h1 class="brand-title" style="font-size: 56px; line-height: 0.95; margin: 24px 0 14px">
          A refined control room for products, stories and orders.
        </h1>
        <p style="color: #d2c3b7">后台不需要浮夸，但要有秩序感、品牌感和清晰的数据入口。</p>
        <div class="hero-card panel" style="min-height: 260px; margin-top: 24px; background: linear-gradient(135deg, #2c241f, #4e3828)"></div>
      </section>
      <section class="panel" style="padding: 36px; display: grid; gap: 18px">
        <h2 class="brand-title" style="font-size: 40px; margin: 0">Sign in</h2>
        <label style="display: grid; gap: 8px">
          <span>账号</span>
          <input v-model="form.username" class="input" placeholder="请输入管理员账号" />
        </label>
        <label style="display: grid; gap: 8px">
          <span>密码</span>
          <input v-model="form.password" class="input" type="password" placeholder="请输入密码" />
        </label>
        <p v-if="error" class="section-subtitle">{{ error }}</p>
        <div style="display: flex; gap: 12px">
          <button class="btn primary" @click="login">进入后台</button>
          <RouterLink class="btn secondary" to="/">返回站点</RouterLink>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { authApi } from "../../services/api";

const router = useRouter();
const route = useRoute();
const error = ref("");
const form = reactive({
  username: "",
  password: ""
});

async function login() {
  try {
    await authApi.adminLogin(form);
    router.push(route.query.redirect || "/admin");
  } catch (err) {
    error.value = err.message;
  }
}
</script>
