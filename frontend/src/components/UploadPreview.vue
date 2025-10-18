<template>
  <aside class="preview" aria-live="polite">
    <header>
      <h2>上传记录回显</h2>
      <p>在这里查看刚刚提交的文件详情以及后端返回的结果。</p>
    </header>

    <div v-if="!upload">
      <div class="empty-state">
        <svg aria-hidden="true" viewBox="0 0 24 24">
          <path
            fill="currentColor"
            d="M12 2a7 7 0 0 1 7 7v1h1a2 2 0 0 1 2 2v6.5A3.5 3.5 0 0 1 18.5 22h-13A3.5 3.5 0 0 1 2 18.5V12a2 2 0 0 1 2-2h1V9a7 7 0 0 1 7-7m0 2a5 5 0 0 0-5 5v1h10V9a5 5 0 0 0-5-5m-1 12v3h2v-3h3l-4-4l-4 4z"
          />
        </svg>
        <p>等待上传记录...</p>
      </div>
    </div>

    <div v-else class="details">
      <dl>
        <div>
          <dt>用户编号</dt>
          <dd>{{ upload.userId }}</dd>
        </div>
        <div>
          <dt>文件名称</dt>
          <dd>{{ upload.fileName }}</dd>
        </div>
        <div>
          <dt>文件类型</dt>
          <dd>{{ humanReadableType }}</dd>
        </div>
        <div>
          <dt>文件大小</dt>
          <dd>{{ humanReadableSize }}</dd>
        </div>
        <div>
          <dt>上传时间</dt>
          <dd>{{ formatDate(upload.submittedAt) }}</dd>
        </div>
      </dl>

      <section class="response">
        <h3>后端响应</h3>
        <div class="code-block">
          <pre>{{ formattedResponse }}</pre>
        </div>
      </section>
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  upload: {
    type: Object,
    default: null
  }
});

const humanReadableSize = computed(() => {
  if (!props.upload) return '-';
  const size = Number(props.upload.fileSize);
  if (!Number.isFinite(size)) return props.upload.fileSize;
  if (size < 1024) return `${size.toFixed(0)} B`;
  if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`;
  if (size < 1024 * 1024 * 1024) return `${(size / 1024 / 1024).toFixed(1)} MB`;
  return `${(size / 1024 / 1024 / 1024).toFixed(1)} GB`;
});

const humanReadableType = computed(() => {
  if (!props.upload) return '-';
  return props.upload.fileType || '未知类型';
});

const formattedResponse = computed(() => {
  if (!props.upload) return '';
  return JSON.stringify(props.upload.serverResponse, null, 2);
});

const formatDate = (value) => {
  if (!value) return '-';
  try {
    return new Intl.DateTimeFormat('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    }).format(new Date(value));
  } catch (error) {
    return value;
  }
};
</script>

<style scoped>
.preview {
  width: min(420px, 100%);
  background: rgba(15, 23, 42, 0.92);
  color: #f8fafc;
  border-radius: 28px;
  padding: 2.4rem 2.2rem;
  box-shadow: 0 24px 50px rgba(15, 23, 42, 0.35);
  display: flex;
  flex-direction: column;
  gap: 1.8rem;
}

header h2 {
  margin: 0;
  font-size: 1.4rem;
  font-weight: 700;
}

header p {
  margin: 0.4rem 0 0;
  font-size: 0.95rem;
  color: rgba(241, 245, 249, 0.78);
  line-height: 1.6;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  padding: 2.5rem 1.5rem;
  border: 1px dashed rgba(148, 163, 184, 0.35);
  border-radius: 22px;
  background: rgba(148, 163, 184, 0.08);
}

.empty-state svg {
  width: 48px;
  height: 48px;
  color: #38bdf8;
}

.empty-state p {
  margin: 0;
  font-size: 0.95rem;
  color: rgba(241, 245, 249, 0.85);
}

.details dl {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 1.2rem 1.4rem;
  margin: 0;
}

.details dt {
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: rgba(148, 163, 184, 0.8);
  margin-bottom: 0.2rem;
}

.details dd {
  margin: 0;
  font-size: 1.05rem;
  font-weight: 600;
  word-break: break-all;
}

.response {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}

.response h3 {
  margin: 0;
  font-size: 1.05rem;
}

.code-block {
  background: rgba(15, 23, 42, 0.7);
  border-radius: 18px;
  padding: 1rem 1.2rem;
  border: 1px solid rgba(148, 163, 184, 0.15);
  overflow-x: auto;
}

.code-block pre {
  margin: 0;
  font-family: 'JetBrains Mono', 'Fira Code', monospace;
  font-size: 0.85rem;
  color: #f8fafc;
  white-space: pre-wrap;
}

@media (max-width: 960px) {
  .preview {
    width: 100%;
    padding: 2rem 1.8rem;
  }

  .details dl {
    grid-template-columns: 1fr;
  }
}
</style>
