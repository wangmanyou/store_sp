<template>
  <div class="grid cols-2">
    <section class="hero-card panel" style="padding: 34px; min-height: 540px; background: linear-gradient(135deg, #d8c0aa, #f4e7d7)">
      <span class="chip" style="width: fit-content; background: #fff8f1; border: none; color: #5b4332">用户端入口</span>
      <h1 class="brand-title" style="font-size: 58px; line-height: 0.95; margin: 24px 0 14px">
        进入简洁、可信赖的校园电商体验
      </h1>
      <p class="section-subtitle">支持商品浏览、购物车、订单确认与个人订单查询。</p>
      <div class="hero-card panel" style="min-height: 240px; margin-top: 24px; background: linear-gradient(135deg, #8f674b, #d8c0aa)"></div>
    </section>

    <section class="panel" style="padding: 36px; display: grid; gap: 18px">
      <h2 class="brand-title" style="font-size: 40px; margin: 0">欢迎回来</h2>
      <p class="section-subtitle">使用用户名和密码进入商城系统</p>
      <label style="display: grid; gap: 8px">
        <span>用户名</span>
        <input v-model="form.username" class="input" placeholder="请输入用户名" />
      </label>
      <label style="display: grid; gap: 8px">
        <span>密码</span>
        <input v-model="form.password" class="input" type="password" placeholder="请输入密码" />
      </label>
      <p v-if="error" class="section-subtitle">{{ error }}</p>
      <div style="display: flex; gap: 12px">
        <button class="btn primary" @click="login">立即登录</button>
        <button class="btn secondary" @click="register">注册账号</button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { authApi } from "../../services/api";

const router = useRouter();
const error = ref("");
const form = reactive({
  username: "",
  password: ""
});

async function login() {
  try {
    await authApi.userLogin(form);
    router.push("/products");
  } catch (err) {
    error.value = err.message;
  }
}

async function register() {
  try {
    await authApi.userRegister({
      username: form.username,
      password: form.password,
      nickname: form.username,
      phone: "13800000000"
    });
    router.push("/products");
  } catch (err) {
    error.value = err.message;
  }
}
</script>
