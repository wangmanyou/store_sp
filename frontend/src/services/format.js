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
