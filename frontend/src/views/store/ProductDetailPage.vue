<template>
  <div class="grid cols-2">
    <section class="product-gallery" @mouseenter="stopAutoPlay" @mouseleave="startAutoPlay">
      <div class="panel visual-card product-gallery-main" :style="{ background: coverStyle(activeImage) }">
        <button v-if="galleryImages.length > 1" class="gallery-nav prev" type="button" @click="prevImage">‹</button>
        <button v-if="galleryImages.length > 1" class="gallery-nav next" type="button" @click="nextImage">›</button>
      </div>
      <div class="product-gallery-meta">
        <span class="chip active">商品图集</span>
        <span class="section-subtitle">{{ galleryImages.length }} 张图片</span>
      </div>
      <div
        v-if="galleryImages.length"
        class="product-gallery-strip"
        @mousedown="startDrag"
        @mouseleave="stopDrag"
        @mouseup="stopDrag"
        @mousemove="drag"
        @touchstart="startTouchDrag"
        @touchmove="touchDrag"
        @touchend="stopDrag"
      >
        <button
          v-for="image in galleryImages"
          :key="image"
          class="panel product-gallery-thumb"
          :class="{ active: image === activeImage }"
          :style="{ background: coverStyle(image) }"
          type="button"
          @click="selectImage(image)"
        ></button>
      </div>
      <div v-else class="panel product-gallery-empty">暂无商品图集</div>
    </section>

    <section class="panel" style="padding: 30px; display: grid; gap: 18px">
      <span class="chip" style="width: fit-content; background: #f1e5d8; border: none; color: #6b4e39">LIMITED EDITION</span>
      <h1 class="brand-title" style="font-size: 50px; line-height: 0.95; margin: 0">{{ product.name }}</h1>
      <p class="section-subtitle">{{ product.subtitle || "精选商品详情" }}</p>
      <strong style="font-size: 22px">¥{{ money(product.price) }}</strong>

      <div class="soft-panel" style="padding: 18px; display: grid; gap: 12px">
        <span>库存：{{ product.stock }}</span>
        <span>销量：{{ product.sales }}</span>
        <span>分类：{{ product.categoryName }}</span>
        <span>配送：24 小时内发货，支持 7 天无理由</span>
      </div>

      <div style="display: flex; align-items: center; gap: 14px">
        <strong>数量</strong>
        <el-input-number v-model="quantity" class="brand-number" :min="1" :max="product.stock || 999" />
      </div>

      <div style="display: flex; gap: 12px">
        <button class="btn primary" @click="addCart">加入购物袋</button>
        <button class="btn secondary">收藏</button>
      </div>

      <div class="panel" style="padding: 18px">
        <h2 class="brand-title" style="font-size: 30px; margin: 0 0 10px">商品详情</h2>
        <p class="section-subtitle">{{ product.detail || "暂无详情介绍" }}</p>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import { useRoute } from "vue-router";
import { cartApi, storeApi } from "../../services/api";
import { coverStyle, money, productImages } from "../../services/format";

const route = useRoute();
const product = ref({});
const quantity = ref(1);
const activeImage = ref("");
const galleryStrip = ref(null);
const dragState = ref({ active: false, x: 0, left: 0 });
let autoTimer = null;

const galleryImages = computed(() => productImages(product.value));

async function loadDetail() {
  product.value = await storeApi.getProductDetail(route.params.id);
  activeImage.value = galleryImages.value[0] || "";
  startAutoPlay();
}

async function addCart() {
  try {
    await cartApi.addCart({ productId: product.value.id, quantity: quantity.value });
    ElMessage.success("已加入购物袋");
  } catch (err) {
    ElMessage.warning(err.message || "请先登录后再加入购物袋");
  }
}

function startDrag(event) {
  galleryStrip.value = event.currentTarget;
  dragState.value = {
    active: true,
    x: event.pageX,
    left: galleryStrip.value.scrollLeft
  };
}

function stopDrag() {
  dragState.value.active = false;
}

function drag(event) {
  if (!dragState.value.active || !galleryStrip.value) {
    return;
  }
  event.preventDefault();
  const distance = event.pageX - dragState.value.x;
  galleryStrip.value.scrollLeft = dragState.value.left - distance;
}

function startTouchDrag(event) {
  galleryStrip.value = event.currentTarget;
  dragState.value = {
    active: true,
    x: event.touches[0].pageX,
    left: galleryStrip.value.scrollLeft
  };
}

function touchDrag(event) {
  if (!dragState.value.active || !galleryStrip.value) {
    return;
  }
  const distance = event.touches[0].pageX - dragState.value.x;
  galleryStrip.value.scrollLeft = dragState.value.left - distance;
}

function selectImage(image) {
  activeImage.value = image;
  restartAutoPlay();
}

function nextImage() {
  if (galleryImages.value.length <= 1) {
    return;
  }
  const currentIndex = galleryImages.value.indexOf(activeImage.value);
  const nextIndex = (currentIndex + 1) % galleryImages.value.length;
  activeImage.value = galleryImages.value[nextIndex];
}

function prevImage() {
  if (galleryImages.value.length <= 1) {
    return;
  }
  const currentIndex = galleryImages.value.indexOf(activeImage.value);
  const prevIndex = (currentIndex - 1 + galleryImages.value.length) % galleryImages.value.length;
  activeImage.value = galleryImages.value[prevIndex];
}

function startAutoPlay() {
  stopAutoPlay();
  if (galleryImages.value.length <= 1) {
    return;
  }
  autoTimer = window.setInterval(nextImage, 3500);
}

function stopAutoPlay() {
  if (autoTimer) {
    window.clearInterval(autoTimer);
    autoTimer = null;
  }
}

function restartAutoPlay() {
  stopAutoPlay();
  startAutoPlay();
}

watch(galleryImages, (images) => {
  if (!images.includes(activeImage.value)) {
    activeImage.value = images[0] || "";
  }
  restartAutoPlay();
});

onMounted(loadDetail);
onUnmounted(stopAutoPlay);
</script>
