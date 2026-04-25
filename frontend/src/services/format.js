export const fallbackCover = "linear-gradient(135deg, #d8c0aa, #8f674b)";

export function coverStyle(image) {
  if (!image) {
    return fallbackCover;
  }
  if (image.startsWith("http") || image.startsWith("/") || image.startsWith("data:")) {
    return `url(${image}) center / cover`;
  }
  return image;
}

export function money(value) {
  return Number(value || 0).toFixed(2);
}

export function imageList(value) {
  if (!value) {
    return [];
  }
  if (Array.isArray(value)) {
    return value.filter(Boolean);
  }
  return String(value)
    .split(",")
    .map((item) => item.trim())
    .filter(Boolean);
}

export function productImages(product = {}) {
  return Array.from(new Set([product.coverImage, ...imageList(product.images)].filter(Boolean)));
}

export function orderStatusText(status) {
  const map = {
    0: "待付款",
    1: "待发货",
    2: "已发货",
    3: "已完成",
    4: "已取消"
  };
  return map[status] || "未知";
}
