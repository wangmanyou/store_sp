<template>
  <div class="brand-upload">
    <div class="brand-upload-preview" :style="{ background: coverStyle(modelValue) }">
      <span v-if="!modelValue">Image</span>
    </div>
    <div class="brand-upload-control">
      <el-upload
        :show-file-list="false"
        :http-request="upload"
        :before-upload="beforeUpload"
        accept="image/jpeg,image/png,image/webp,image/gif"
      >
        <button class="btn secondary" type="button" :disabled="uploading">
          {{ uploading ? "Uploading..." : buttonText }}
        </button>
      </el-upload>
      <el-input
        :model-value="modelValue"
        placeholder="/upload/..."
        @update:model-value="$emit('update:modelValue', $event)"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { adminApi } from "../services/api";
import { coverStyle } from "../services/format";

defineProps({
  modelValue: {
    type: String,
    default: ""
  },
  buttonText: {
    type: String,
    default: "Upload image"
  }
});

const emit = defineEmits(["update:modelValue"]);
const uploading = ref(false);

function beforeUpload(file) {
  const validType = ["image/jpeg", "image/png", "image/webp", "image/gif"].includes(file.type);
  if (!validType) {
    ElMessage.warning("Please upload a JPG, PNG, WebP or GIF image");
    return false;
  }
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.warning("Image must be smaller than 10MB");
    return false;
  }
  return true;
}

async function upload({ file }) {
  uploading.value = true;
  try {
    const result = await adminApi.uploadImage(file);
    emit("update:modelValue", result.url);
    ElMessage.success("Image uploaded");
  } catch (err) {
    ElMessage.error(err.message || "Upload failed");
  } finally {
    uploading.value = false;
  }
}
</script>
