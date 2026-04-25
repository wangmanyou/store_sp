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
      if (result?.code === 401 || result?.code === 403) {
        clearToken(response.config.url?.startsWith("/admin") ? "ADMIN" : "USER");
      }
      return Promise.reject(new Error(result?.message || "请求失败"));
    }
    return result.data;
  },
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      clearToken(error.config?.url?.startsWith("/admin") ? "ADMIN" : "USER");
    }
    return Promise.reject(new Error(error.response?.data?.message || error.message || "网络异常"));
  }
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

export function getAdminSession() {
  const token = localStorage.getItem(TOKEN_KEYS.admin);
  const userInfoText = localStorage.getItem(ADMIN_INFO_KEY);
  return {
    token,
    userInfo: userInfoText ? JSON.parse(userInfoText) : null
  };
}

export function hasValidAdminSession() {
  return hasValidJwtSession(getAdminSession().token, "ADMIN");
}

export function hasValidUserSession() {
  return hasValidJwtSession(getUserSession().token, "USER");
}

function hasValidJwtSession(token, role) {
  if (!token) {
    return false;
  }
  const parts = token.split(".");
  if (parts.length !== 3) {
    return false;
  }
  try {
    const payload = JSON.parse(decodeBase64Url(parts[1]));
    const now = Math.floor(Date.now() / 1000);
    return payload.role === role && Number(payload.exp || 0) > now;
  } catch {
    return false;
  }
}

function decodeBase64Url(value) {
  const base64 = value.replace(/-/g, "+").replace(/_/g, "/").padEnd(Math.ceil(value.length / 4) * 4, "=");
  const binary = atob(base64);
  return decodeURIComponent(
    Array.from(binary)
      .map((char) => `%${char.charCodeAt(0).toString(16).padStart(2, "0")}`)
      .join("")
  );
}

export function clearToken(role) {
  const isAdmin = role === "ADMIN";
  localStorage.removeItem(isAdmin ? TOKEN_KEYS.admin : TOKEN_KEYS.user);
  localStorage.removeItem(isAdmin ? ADMIN_INFO_KEY : USER_INFO_KEY);
  window.dispatchEvent(new CustomEvent(AUTH_CHANGED_EVENT));
}

export default request;
