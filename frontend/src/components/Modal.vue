<template>
  <div class="modal-overlay" v-if="show" @wheel.stop @touchmove.stop>
    <div class="modal-content" @wheel.stop @touchmove.stop>
      <slot></slot>
      <button class="btn-close" @click="$emit('close')">X</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { watch, ref } from 'vue'

const props = defineProps<{ show: boolean }>();
const emit = defineEmits(['close']);

// 当 Modal 打开时，禁止 body 滚动
watch(
  () => props.show,
  (newVal) => {
    if (newVal) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
  },
  { immediate: true }
);
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: var(--secondary-color);
  padding: 2rem;
  border-radius: var(--border-radius);
  position: relative;
  width: 80%;
  max-width: 500px;
}

.btn-close {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  color: var(--text-color);
  font-size: 1.5rem;
  cursor: pointer;
}
</style>
