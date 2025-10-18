<template>
  <form class="upload-card" @submit.prevent="handleSubmit">
    <h1 class="title">智能文件管理中心</h1>
    <p class="subtitle">
      上传学习资料、作业或项目文件，我们会安全存储并提供实时回显。
    </p>

    <div class="input-group">
      <label for="user-id">用户编号</label>
      <div class="input-wrapper">
        <span class="prefix">UID</span>
        <input
          id="user-id"
          v-model.trim="userId"
          type="number"
          min="1"
          required
          placeholder="请输入用户ID"
        />
      </div>
      <p v-if="errors.userId" class="error">{{ errors.userId }}</p>
    </div>

    <div class="input-group">
      <label for="file">上传文件</label>
      <label class="file-drop" :class="{ 'is-dragover': isDragOver }">
        <input
          id="file"
          ref="fileInput"
          type="file"
          required
          @change="onFileChange"
          @dragenter.prevent="isDragOver = true"
          @dragover.prevent="isDragOver = true"
          @dragleave.prevent="isDragOver = false"
          @drop.prevent="handleDrop"
        />
        <div class="file-drop__content">
          <svg aria-hidden="true" viewBox="0 0 24 24">
            <path
              fill="currentColor"
              d="M12 3a5 5 0 0 1 5 5v2h1a3 3 0 0 1 3 3v6a3 3 0 0 1-3 3H8a5 5 0 0 1-5-5v-4a5 5 0 0 1 5-5h1V8a5 5 0 0 1 5-5m0 2a3 3 0 0 0-3 3v2h6V8a3 3 0 0 0-3-3m-4 7a3 3 0 0 0-3 3v2a3 3 0 0 0 3 3h10a1 1 0 0 0 1-1v-6a1 1 0 0 0-1-1z"
            />
          </svg>
          <div>
            <strong>{{ fileName || '点击或拖拽文件到此处' }}</strong>
            <p>支持任意格式的单文件上传</p>
          </div>
        </div>
      </label>
      <p v-if="errors.file" class="error">{{ errors.file }}</p>
    </div>

    <button class="submit" type="submit" :disabled="isSubmitting">
      <span v-if="!isSubmitting">立即上传</span>
      <span v-else class="loading">
        <svg aria-hidden="true" viewBox="0 0 24 24">
          <circle class="path" cx="12" cy="12" r="9" />
        </svg>
        上传中...
      </span>
    </button>

    <transition name="fade">
      <p v-if="status.message" class="status" :class="status.type">
        {{ status.message }}
      </p>
    </transition>
  </form>
</template>

<script setup>
import { reactive, ref } from 'vue';

const emit = defineEmits(['upload-success', 'upload-error']);

const userId = ref('');
const fileInput = ref(null);
const file = ref(null);
const fileName = ref('');
const isSubmitting = ref(false);
const isDragOver = ref(false);
const errors = reactive({ userId: '', file: '' });
const status = reactive({ message: '', type: '' });

const resetStatus = () => {
  status.message = '';
  status.type = '';
};

const validate = () => {
  errors.userId = '';
  errors.file = '';
  let valid = true;

  if (!userId.value) {
    errors.userId = '请填写用户编号。';
    valid = false;
  }

  if (!file.value) {
    errors.file = '请选择需要上传的文件。';
    valid = false;
  }

  return valid;
};

const onFileChange = (event) => {
  resetStatus();
  const files = event.target.files;
  if (files && files.length) {
    file.value = files[0];
    fileName.value = files[0].name;
  }
  isDragOver.value = false;
};

const handleDrop = (event) => {
  resetStatus();
  const droppedFiles = event.dataTransfer.files;
  if (droppedFiles && droppedFiles.length) {
    file.value = droppedFiles[0];
    fileName.value = droppedFiles[0].name;
    if (fileInput.value) {
      fileInput.value.files = droppedFiles;
    }
  }
  isDragOver.value = false;
};

const handleSubmit = async () => {
  resetStatus();
  if (!validate()) {
    return;
  }

  isSubmitting.value = true;
  try {
    const formData = new FormData();
    formData.append('file', file.value);
    formData.append('userId', userId.value);

    const response = await fetch('/api/files', {
      method: 'POST',
      body: formData
    });

    if (!response.ok) {
      throw new Error('网络请求失败');
    }

    const result = await response.json();
    if (result.code !== 1) {
      throw new Error(result.msg || '文件上传失败');
    }

    status.message = '上传成功！';
    status.type = 'success';

    emit('upload-success', {
      userId: userId.value,
      fileName: file.value.name,
      fileSize: file.value.size,
      fileType: file.value.type || file.value.name.split('.').pop(),
      submittedAt: new Date().toISOString(),
      serverResponse: result
    });
  } catch (error) {
    status.message = error.message || '上传失败，请稍后重试。';
    status.type = 'error';
    emit('upload-error', error);
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
.upload-card {
  width: min(520px, 100%);
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(16px);
  border-radius: 28px;
  padding: 2.5rem 3rem;
  box-shadow: 0 20px 45px rgba(15, 23, 42, 0.12);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.title {
  margin: 0;
  font-size: 1.8rem;
  font-weight: 700;
  color: #1d3b8b;
}

.subtitle {
  margin: 0;
  font-size: 0.98rem;
  color: #4b5563;
  line-height: 1.6;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}

.input-group label {
  font-weight: 600;
  color: #1f2937;
}

.input-wrapper {
  display: flex;
  align-items: center;
  background: #f4f6fb;
  border: 1px solid transparent;
  border-radius: 16px;
  padding: 0.2rem 0.75rem;
  transition: border-color 0.2s ease;
}

.input-wrapper:focus-within {
  border-color: #365cf5;
  box-shadow: 0 0 0 4px rgba(54, 92, 245, 0.15);
}

.prefix {
  font-size: 0.85rem;
  font-weight: 600;
  color: #6b7280;
  margin-right: 0.5rem;
}

input[type='number'] {
  flex: 1;
  border: none;
  background: transparent;
  font-size: 1rem;
  padding: 0.75rem 0;
  outline: none;
  color: #111827;
}

.file-drop {
  border: 2px dashed #cbd5f5;
  border-radius: 20px;
  background: rgba(236, 243, 255, 0.6);
  padding: 1.75rem;
  text-align: center;
  transition: all 0.2s ease;
  cursor: pointer;
}

.file-drop input[type='file'] {
  display: none;
}

.file-drop__content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  color: #475569;
}

.file-drop svg {
  width: 48px;
  height: 48px;
  color: #365cf5;
}

.file-drop strong {
  font-size: 1.05rem;
  color: #1e293b;
}

.file-drop p {
  margin: 0;
  font-size: 0.85rem;
  color: #6b7280;
}

.file-drop.is-dragover {
  border-color: #365cf5;
  background: rgba(54, 92, 245, 0.1);
}

.submit {
  margin-top: 0.5rem;
  background: linear-gradient(135deg, #4f46e5, #2563eb);
  color: #fff;
  border: none;
  border-radius: 999px;
  padding: 0.9rem 1.5rem;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease, opacity 0.2s ease;
}

.submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 12px 24px rgba(37, 99, 235, 0.25);
}

.submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.loading svg {
  width: 20px;
  height: 20px;
  animation: spin 1s linear infinite;
}

.loading .path {
  fill: none;
  stroke: currentColor;
  stroke-width: 4;
  stroke-linecap: round;
  stroke-dasharray: 45;
  stroke-dashoffset: 0;
}

.error {
  margin: 0;
  color: #dc2626;
  font-size: 0.85rem;
}

.status {
  margin: 0;
  padding: 0.75rem 1rem;
  border-radius: 12px;
  font-size: 0.9rem;
  text-align: center;
}

.status.success {
  background: #ecfdf5;
  color: #059669;
}

.status.error {
  background: #fef2f2;
  color: #dc2626;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 640px) {
  .upload-card {
    padding: 2rem 1.5rem;
    border-radius: 22px;
  }

  .title {
    font-size: 1.5rem;
  }
}
</style>
