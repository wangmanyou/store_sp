import { createRouter, createWebHistory } from "vue-router";
import StoreLayout from "../layouts/StoreLayout.vue";
import AdminLayout from "../layouts/AdminLayout.vue";
import HomePage from "../views/store/HomePage.vue";
import ProductListPage from "../views/store/ProductListPage.vue";
import ProductDetailPage from "../views/store/ProductDetailPage.vue";
import CartPage from "../views/store/CartPage.vue";
import LoginPage from "../views/store/LoginPage.vue";
import AdminLoginPage from "../views/admin/AdminLoginPage.vue";
import AdminDashboardPage from "../views/admin/AdminDashboardPage.vue";
import AdminCategoryPage from "../views/admin/AdminCategoryPage.vue";
import AdminBannerPage from "../views/admin/AdminBannerPage.vue";
import AdminProductPage from "../views/admin/AdminProductPage.vue";
import AdminOrderPage from "../views/admin/AdminOrderPage.vue";
import AdminUserPage from "../views/admin/AdminUserPage.vue";

const routes = [
  {
    path: "/",
    component: StoreLayout,
    children: [
      { path: "", name: "home", component: HomePage },
      { path: "products", name: "products", component: ProductListPage },
      { path: "product/:id", name: "product-detail", component: ProductDetailPage },
      { path: "cart", name: "cart", component: CartPage },
      { path: "login", name: "login", component: LoginPage }
    ]
  },
  {
    path: "/admin/login",
    name: "admin-login",
    component: AdminLoginPage
  },
  {
    path: "/admin",
    component: AdminLayout,
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

export default createRouter({
  history: createWebHistory(),
  routes
});
