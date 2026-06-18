<template>
  <div class="supplement-tracker-container">
    <div class="tracker-header">
      <h2>补剂记录</h2>
      <p class="header-desc">追踪你的营养补给</p>
    </div>

    <div class="supplement-templates">
      <h3>常用补剂</h3>
      <div class="templates-grid">
        <div
          v-for="template in templates"
          :key="template.name"
          :class="['template-card', { selected: selectedTemplate?.name === template.name }]"
          @click="selectTemplate(template)"
        >
          <div class="template-icon">{{ getSupplementIcon(template.name) }}</div>
          <div class="template-info">
            <span class="template-name">{{ template.name }}</span>
            <span class="template-unit">单位: {{ template.unit }}</span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="selectedTemplate" class="supplement-input">
      <div class="input-group">
        <label>服用量</label>
        <div class="input-wrapper">
          <input
            v-model="dosage"
            type="text"
            placeholder="例如: 5g 或 1勺"
          >
        </div>
      </div>

      <button @click="logSupplement" class="btn-log" :disabled="!dosage">
        <span class="btn-icon">💊</span>
        <span>记录服用</span>
      </button>
    </div>

    <div v-if="supplementLogs.length > 0" class="supplement-history">
      <h3>今日记录</h3>
      <div class="history-list">
        <div
          v-for="log in todayLogs"
          :key="log.id"
          class="history-item"
        >
          <div class="log-icon">{{ getSupplementIcon(log.name) }}</div>
          <div class="log-info">
            <span class="log-name">{{ log.name }}</span>
            <span class="log-dosage">{{ log.dosage }}</span>
          </div>
          <div class="log-time">{{ formatTime(log.takenTime) }}</div>
        </div>
      </div>
    </div>

    <div v-if="supplementLogs.length === 0" class="empty-state">
      <div class="empty-icon">💊</div>
      <p>暂无补剂记录</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'

interface SupplementTemplate {
  name: string
  unit: string
}

interface SupplementLog {
  id: number
  name: string
  dosage: string
  takenTime: string
}

const templates = ref<SupplementTemplate[]>([])
const selectedTemplate = ref<SupplementTemplate | null>(null)
const dosage = ref('')
const supplementLogs = ref<SupplementLog[]>([])
const sessionId = ref<number | null>(null)

const todayLogs = computed(() => {
  const today = new Date().toDateString()
  return supplementLogs.value.filter(log =>
    new Date(log.takenTime).toDateString() === today
  )
})

function getSupplementIcon(name: string): string {
  const icons: Record<string, string> = {
    '肌酸': '💪',
    '蛋白粉': '🥛',
    '氮泵': '⚡',
    '咖啡因': '☕'
  }
  return icons[name] || '💊'
}

function selectTemplate(template: SupplementTemplate) {
  selectedTemplate.value = template
  dosage.value = ''
}

async function fetchTemplates() {
  try {
    const response = await fetch('/api/supplement/templates')
    const data = await response.json()

    if (data.code === 200) {
      templates.value = data.data || []
    }
  } catch (error) {
    console.error('获取补剂模板失败:', error)
  }
}

async function logSupplement() {
  if (!selectedTemplate.value || !dosage.value) return

  try {
    const response = await fetch('/api/supplement/log', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        sessionId: sessionId.value || 1,
        name: selectedTemplate.value.name,
        dosage: dosage.value,
        takenTime: new Date().toISOString()
      })
    })

    const data = await response.json()

    if (data.code === 200) {
      supplementLogs.value.push({
        id: Date.now(),
        name: selectedTemplate.value.name,
        dosage: dosage.value,
        takenTime: new Date().toISOString()
      })
      dosage.value = ''
      selectedTemplate.value = null
    }
  } catch (error) {
    console.error('记录补剂失败:', error)
  }
}

function formatTime(timeString: string): string {
  if (!timeString) return ''
  return new Date(timeString).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

onMounted(() => {
  fetchTemplates()
})
</script>

<style scoped>
.supplement-tracker-container {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.05), rgba(255, 255, 255, 0.02));
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 24px;
}

.tracker-header {
  margin-bottom: 24px;
}

.tracker-header h2 {
  margin: 0;
  font-size: 20px;
  color: var(--text-color);
}

.header-desc {
  margin: 8px 0 0;
  color: #888;
  font-size: 14px;
}

.supplement-templates {
  margin-bottom: 24px;
}

.supplement-templates h3 {
  margin: 0 0 16px;
  font-size: 16px;
  color: var(--text-color);
}

.templates-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 12px;
}

.template-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 12px;
}

.template-card:hover {
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(255, 71, 87, 0.2);
}

.template-card.selected {
  background: rgba(255, 71, 87, 0.15);
  border-color: rgba(255, 71, 87, 0.5);
  box-shadow: 0 4px 12px rgba(255, 71, 87, 0.2);
}

.template-icon {
  font-size: 28px;
}

.template-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.template-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-color);
}

.template-unit {
  font-size: 12px;
  color: #888;
}

.supplement-input {
  display: flex;
  gap: 16px;
  align-items: end;
  margin-bottom: 24px;
}

.input-group {
  flex: 1;
}

.input-group label {
  display: block;
  margin-bottom: 8px;
  color: #e0e0e0;
  font-size: 14px;
}

.input-wrapper input {
  width: 100%;
  padding: 12px 16px;
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-color);
  font-size: 16px;
}

.input-wrapper input:focus {
  outline: none;
  border-color: rgba(255, 71, 87, 0.5);
}

.btn-log {
  padding: 12px 24px;
  border-radius: 10px;
  background: linear-gradient(135deg, #ff4757, #ff6b7a);
  border: none;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(255, 71, 87, 0.3);
}

.btn-log:hover:not(:disabled) {
  transform: translateY(-2px);
}

.btn-log:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.supplement-history {
  margin-top: 24px;
}

.supplement-history h3 {
  margin: 0 0 16px;
  font-size: 16px;
  color: var(--text-color);
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.history-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px;
}

.log-icon {
  font-size: 24px;
}

.log-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.log-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-color);
}

.log-dosage {
  font-size: 12px;
  color: #888;
}

.log-time {
  font-size: 12px;
  color: #888;
}

.empty-state {
  text-align: center;
  padding: 40px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-state p {
  color: #888;
}
</style>