import axios from "axios";

const TOKEN_KEYS = {
  user: "store_user_token",
  admin: "store_admin_token"
};

const USER_INFO_KEY = "store_user_info";
const ADMIN_INFO_KEY = "store_admin_info";

export const AUTH_CHANGED_EVENT = "store-auth-changed";

const request = axios.create({
  baseURL: "/api",
  timeout: 10000
});

request.interceptors.request.use((config) => {
  const tokenKey = config.url?.startsWith("/admin") ? TOKEN_KEYS.admin : TOKEN_KEYS.user;
  const token = localStorage.getItem(tokenKey);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

request.interceptors.response.use(
  (response) => {
    const result = response.data;
    if (result?.code !== 200) {
      return Promise.reject(new Error(result?.message || "请求失败"));
    }
    return result.data;
  },
  (error) => Promise.reject(new Error(error.response?.data?.message || error.message || "网络异常"))
);

export function setSession(role, token, userInfo) {
  const isAdmin = role === "ADMIN";
  localStorage.setItem(isAdmin ? TOKEN_KEYS.admin : TOKEN_KEYS.user, token);
  localStorage.setItem(isAdmin ? ADMIN_INFO_KEY : USER_INFO_KEY, JSON.stringify(userInfo || {}));
  window.dispatchEvent(new CustomEvent(AUTH_CHANGED_EVENT));
}

export function getUserSession() {
  const token = localStorage.getItem(TOKEN_KEYS.user);
  const userInfoText = localStorage.getItem(USER_INFO_KEY);
  return {
    token,
    userInfo: userInfoText ? JSON.parse(userInfoText) : null
  };
}

export function clearToken(role) {
  const isAdmin = role === "ADMIN";
  localStorage.removeItem(isAdmin ? TOKEN_KEYS.admin : TOKEN_KEYS.user);
  localStorage.removeItem(isAdmin ? ADMIN_INFO_KEY : USER_INFO_KEY);
  window.dispatchEvent(new CustomEvent(AUTH_CHANGED_EVENT));
}

export default request;
