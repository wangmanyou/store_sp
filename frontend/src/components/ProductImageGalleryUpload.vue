<template>
  <div class="gallery-upload">
    <div v-if="images.length" class="gallery-upload-list">
      <article v-for="image in images" :key="image" class="gallery-upload-item">
        <div class="gallery-upload-thumb" :style="{ background: coverStyle(image) }"></div>
        <button class="gallery-remove" type="button" @click="removeImage(image)">移除</button>
      </article>
    </div>
    <div v-else class="gallery-upload-empty">暂无图集图片</div>

    <div class="gallery-upload-actions">
      <el-upload
        multiple
        :show-file-list="false"
        :http-request="upload"
        :before-upload="beforeUpload"
        accept="image/jpeg,image/png,image/webp,image/gif"
      >
        <button class="btn secondary" type="button" :disabled="uploading">
          {{ uploading ? "上传中..." : "上传多张图片" }}
        </button>
      </el-upload>
      <el-input
        :model-value="modelValue"
        placeholder="/upload/a.png,/upload/b.png"
        @update:model-value="$emit('update:modelValue', $event)"
      />
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { ElMessage } from "element-plus";
import { adminApi } from "../services/api";
import { coverStyle, imageList } from "../services/format";

const props = defineProps({
  modelValue: {
    type: String,
    default: ""
  }
});

const emit = defineEmits(["update:modelValue"]);
const uploading = ref(false);
const images = computed(() => imageList(props.modelValue));

function beforeUpload(file) {
  const validType = ["image/jpeg", "image/png", "image/webp", "image/gif"].includes(file.type);
  if (!validType) {
    ElMessage.warning("请上传 JPG、PNG、WebP 或 GIF 图片");
    return false;
  }
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.warning("图片不能超过 10MB");
    return false;
  }
  return true;
}

async function upload({ file }) {
  uploading.value = true;
  try {
    const result = await adminApi.uploadImage(file);
    const nextImages = [...images.value, result.url];
    emit("update:modelValue", nextImages.join(","));
    ElMessage.success("图片已加入图集");
  } catch (err) {
    ElMessage.error(err.message || "上传失败");
  } finally {
    uploading.value = false;
  }
}

function removeImage(image) {
  emit("update:modelValue", images.value.filter((item) => item !== image).join(","));
}
</script>
