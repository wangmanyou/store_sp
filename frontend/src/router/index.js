import { createRouter, createWebHistory } from "vue-router";
import StoreLayout from "../layouts/StoreLayout.vue";
import AdminLayout from "../layouts/AdminLayout.vue";
import HomePage from "../views/store/HomePage.vue";
import ProductListPage from "../views/store/ProductListPage.vue";
import ProductDetailPage from "../views/store/ProductDetailPage.vue";
import CartPage from "../views/store/CartPage.vue";
import LoginPage from "../views/store/LoginPage.vue";
import MyAddressPage from "../views/store/MyAddressPage.vue";
import MyOrdersPage from "../views/store/MyOrdersPage.vue";
import AdminLoginPage from "../views/admin/AdminLoginPage.vue";
import AdminDashboardPage from "../views/admin/AdminDashboardPage.vue";
import AdminCategoryPage from "../views/admin/AdminCategoryPage.vue";
import AdminBannerPage from "../views/admin/AdminBannerPage.vue";
import AdminProductPage from "../views/admin/AdminProductPage.vue";
import AdminOrderPage from "../views/admin/AdminOrderPage.vue";
import AdminUserPage from "../views/admin/AdminUserPage.vue";
import { clearToken, hasValidAdminSession, hasValidUserSession } from "../services/request";

const routes = [
  {
    path: "/",
    component: StoreLayout,
    children: [
      { path: "", name: "home", component: HomePage },
      { path: "products", name: "products", component: ProductListPage },
      { path: "product/:id", name: "product-detail", component: ProductDetailPage },
      { path: "cart", name: "cart", component: CartPage, meta: { requiresUser: true } },
      { path: "my-address", name: "my-address", component: MyAddressPage, meta: { requiresUser: true } },
      { path: "my-orders", name: "my-orders", component: MyOrdersPage, meta: { requiresUser: true } },
      { path: "login", name: "login", component: LoginPage, meta: { guestOnlyUser: true } }
    ]
  },
  {
    path: "/admin/login",
    name: "admin-login",
    component: AdminLoginPage,
    meta: { guestOnlyAdmin: true }
  },
  {
    path: "/admin",
    component: AdminLayout,
    meta: { requiresAdmin: true },
    children: [
      { path: "", name: "admin-dashboard", component: AdminDashboardPage },
      { path: "categories", name: "admin-categories", component: AdminCategoryPage },
      { path: "banners", name: "admin-banners", component: AdminBannerPage },
      { path: "products", name: "admin-products", component: AdminProductPage },
      { path: "orders", name: "admin-orders", component: AdminOrderPage },
      { path: "users", name: "admin-users", component: AdminUserPage }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to) => {
  const hasAdminToken = hasValidAdminSession();
  const hasUserToken = hasValidUserSession();

  if (to.meta.requiresAdmin && !hasAdminToken) {
    clearToken("ADMIN");
    return {
      name: "admin-login",
      query: { redirect: to.fullPath }
    };
  }

  if (to.meta.guestOnlyAdmin && hasAdminToken) {
    return { name: "admin-dashboard" };
  }

  if (to.meta.requiresUser && !hasUserToken) {
    clearToken("USER");
    return {
      name: "login",
      query: { redirect: to.fullPath }
    };
  }

  if (to.meta.guestOnlyUser && hasUserToken) {
    return { name: "products" };
  }

  return true;
});

export default router;
