<template>
  <main class="layout">
    <section class="form-area">
      <FileUploadForm @upload-success="handleSuccess" @upload-error="handleError" />
    </section>
    <section class="preview-area">
      <UploadPreview :upload="lastUpload" />
    </section>
  </main>
</template>

<script setup>
import { ref } from 'vue';
import FileUploadForm from './components/FileUploadForm.vue';
import UploadPreview from './components/UploadPreview.vue';

const lastUpload = ref(null);

const handleSuccess = (payload) => {
  lastUpload.value = payload;
};

const handleError = () => {
  // 如果需要，可以在父级处理错误，例如上报或记录日志
};
</script>

<style scoped>
.layout {
  width: min(1080px, 100%);
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 2rem;
  align-items: stretch;
}

.form-area,
.preview-area {
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .layout {
    gap: 1.5rem;
  }
}
</style>
