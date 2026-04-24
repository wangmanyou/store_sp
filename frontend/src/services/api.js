import request, { setSession } from "./request";

export const authApi = {
  async userLogin(data) {
    const result = await request.post("/user/login", data);
    setSession(result.role, result.token, result.userInfo);
    return result;
  },
  async userRegister(data) {
    const result = await request.post("/user/register", data);
    setSession(result.role, result.token, result.userInfo);
    return result;
  },
  async adminLogin(data) {
    const result = await request.post("/admin/login", data);
    setSession(result.role, result.token, result.userInfo);
    return result;
  }
};

export const storeApi = {
  getBanners() {
    return request.get("/banner/list");
  },
  getCategories() {
    return request.get("/category/list");
  },
  getProducts(params = {}) {
    return request.get("/product/list", { params });
  },
  getProductDetail(id) {
    return request.get(`/product/${id}`);
  }
};

export const cartApi = {
  getCart() {
    return request.get("/cart/list");
  },
  addCart(data) {
    return request.post("/cart/add", data);
  },
  updateCart(data) {
    return request.put("/cart/update", data);
  },
  deleteCart(id) {
    return request.delete(`/cart/${id}`);
  }
};

export const adminApi = {
  getBanners(params = {}) {
    return request.get("/admin/banner/list", { params });
  },
  saveBanner(data) {
    return request.post("/admin/banner/save", data);
  },
  updateBanner(data) {
    return request.put("/admin/banner/update", data);
  },
  updateBannerStatus(id, status) {
    return request.put(`/admin/banner/status/${id}`, { status });
  },
  deleteBanner(id) {
    return request.delete(`/admin/banner/${id}`);
  },
  getCategories(params = {}) {
    return request.get("/admin/category/list", { params });
  },
  saveCategory(data) {
    return request.post("/admin/category/save", data);
  },
  updateCategory(data) {
    return request.put("/admin/category/update", data);
  },
  deleteCategory(id) {
    return request.delete(`/admin/category/${id}`);
  },
  getProducts(params = {}) {
    return request.get("/admin/product/list", { params });
  },
  saveProduct(data) {
    return request.post("/admin/product/save", data);
  },
  updateProduct(data) {
    return request.put("/admin/product/update", data);
  },
  updateProductStatus(id, status) {
    return request.put(`/admin/product/status/${id}`, { status });
  },
  deleteProduct(id) {
    return request.delete(`/admin/product/${id}`);
  },
  getOrders(params = {}) {
    return request.get("/admin/order/list", { params });
  },
  deliverOrder(data) {
    return request.put("/admin/order/deliver", data);
  },
  getUsers(params = {}) {
    return request.get("/admin/user/list", { params });
  },
  updateUserStatus(id, status) {
    return request.put(`/admin/user/status/${id}`, { status });
  }
};
