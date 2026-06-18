<template>
  <div class="weight-tracker-container">
    <div class="tracker-header">
      <h2>体重追踪</h2>
      <div class="current-weight">
        <span class="weight-label">当前体重</span>
        <span class="weight-value">{{ latestWeight || '--' }}</span>
        <span class="weight-unit">kg</span>
      </div>
    </div>

    <div class="weight-input-section">
      <div class="input-group">
        <label for="new-weight">记录新体重</label>
        <div class="input-wrapper">
          <input
            id="new-weight"
            v-model.number="newWeight"
            type="number"
            step="0.1"
            placeholder="输入体重"
          >
          <span class="input-unit">kg</span>
        </div>
      </div>
      <button @click="logWeight" class="btn-log" :disabled="!newWeight">
        <span class="btn-icon">⚖️</span>
        <span>记录</span>
      </button>
    </div>

    <div v-if="weightHistory.length > 0" class="weight-history">
      <h3>历史记录</h3>
      <div class="history-chart">
        <Line :data="chartData" :options="chartOptions" />
      </div>

      <div class="history-list">
        <div
          v-for="log in weightHistory.slice().reverse()"
          :key="log.id"
          class="history-item"
        >
          <div class="history-date">{{ formatDate(log.logDate) }}</div>
          <div class="history-weight">{{ log.weight }} kg</div>
          <div v-if="getWeightChange(log) !== null" class="weight-change" :class="getWeightChangeClass(log)">
            {{ getWeightChange(log) > 0 ? '+' : '' }}{{ getWeightChange(log) }} kg
          </div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <div class="empty-icon">📊</div>
      <p>暂无体重记录</p>
      <p class="empty-hint">开始记录你的体重变化吧</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
} from 'chart.js'

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
)

interface WeightLog {
  id: number
  weight: number
  logDate: string
}

const userId = ref(1)
const newWeight = ref<number | null>(null)
const weightHistory = ref<WeightLog[]>([])

const latestWeight = computed(() => {
  if (weightHistory.value.length === 0) return null
  return weightHistory.value[weightHistory.value.length - 1].weight
})

const chartData = computed(() => {
  if (weightHistory.value.length === 0) {
    return { labels: [], datasets: [] }
  }

  return {
    labels: weightHistory.value.map(log => formatDate(log.logDate)),
    datasets: [
      {
        label: '体重',
        backgroundColor: 'rgba(255, 107, 122, 0.1)',
        borderColor: '#ff6b7a',
        data: weightHistory.value.map(log => log.weight),
        tension: 0.4,
        fill: true,
        pointRadius: 4,
        pointHoverRadius: 6,
        pointBackgroundColor: '#ff6b7a',
        pointBorderColor: '#fff',
        pointBorderWidth: 2
      }
    ]
  }
})

const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false,
  scales: {
    x: {
      ticks: { color: '#b0b0b0' },
      grid: { color: 'rgba(255, 255, 255, 0.05)' }
    },
    y: {
      ticks: {
        color: '#b0b0b0',
        callback: (value: any) => `${value} kg`
      },
      grid: { color: 'rgba(255, 255, 255, 0.05)' }
    }
  },
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: 'rgba(30, 30, 30, 0.95)',
      callbacks: {
        label: (context: any) => `${context.parsed.y} kg`
      }
    }
  }
})

async function logWeight() {
  if (!newWeight.value) return

  try {
    const response = await fetch(`/user/weight?userId=${userId.value}&weight=${newWeight.value}`, {
      method: 'POST'
    })

    const data = await response.json()

    if (data.code === 200) {
      await fetchWeightHistory()
      newWeight.value = null
    }
  } catch (error) {
    console.error('记录体重失败:', error)
  }
}

async function fetchWeightHistory() {
  try {
    const response = await fetch(`/user/weight/history?userId=${userId.value}`)
    const data = await response.json()

    if (data.code === 200) {
      weightHistory.value = data.data || []
    }
  } catch (error) {
    console.error('获取体重历史失败:', error)
  }
}

function formatDate(dateString: string): string {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' })
}

function getWeightChange(log: WeightLog): number | null {
  const index = weightHistory.value.findIndex(l => l.id === log.id)
  if (index === 0) return null
  const prevWeight = weightHistory.value[index - 1].weight
  return Math.round((log.weight - prevWeight) * 10) / 10
}

function getWeightChangeClass(log: WeightLog): string {
  const change = getWeightChange(log)
  if (change === null) return ''
  return change > 0 ? 'increase' : change < 0 ? 'decrease' : ''
}

onMounted(() => {
  fetchWeightHistory()
})
</script>

<style scoped>
.weight-tracker-container {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.05), rgba(255, 255, 255, 0.02));
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 24px;
}

.tracker-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.tracker-header h2 {
  margin: 0;
  font-size: 20px;
  color: var(--text-color);
}

.current-weight {
  display: flex;
  align-items: baseline;
  gap: 8px;
  background: rgba(255, 71, 87, 0.1);
  border: 1px solid rgba(255, 71, 87, 0.2);
  border-radius: 10px;
  padding: 10px 16px;
}

.weight-label {
  font-size: 12px;
  color: #888;
}

.weight-value {
  font-size: 24px;
  font-weight: 600;
  color: #ff6b7a;
}

.weight-unit {
  font-size: 14px;
  color: #888;
}

.weight-input-section {
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

.input-wrapper {
  position: relative;
}

.input-wrapper input {
  width: 100%;
  padding: 12px 50px 12px 16px;
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

.input-unit {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #888;
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

.weight-history {
  margin-top: 24px;
}

.weight-history h3 {
  margin: 0 0 16px;
  font-size: 16px;
  color: var(--text-color);
}

.history-chart {
  height: 200px;
  margin-bottom: 20px;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: 300px;
  overflow-y: auto;
}

.history-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px;
}

.history-date {
  font-size: 14px;
  color: #888;
  min-width: 80px;
}

.history-weight {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-color);
}

.weight-change {
  font-size: 14px;
  font-weight: 500;
  padding: 4px 8px;
  border-radius: 6px;
}

.weight-change.increase {
  background: rgba(255, 71, 87, 0.2);
  color: #ff6b7a;
}

.weight-change.decrease {
  background: rgba(76, 175, 80, 0.2);
  color: #81c784;
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
  margin: 8px 0;
}

.empty-hint {
  font-size: 14px;
  color: #666;
}
</style>