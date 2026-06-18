<template>
  <div class="progress-chart-container">
    <div class="chart-header">
      <h2>三大项进步曲线</h2>
      <div class="chart-stats">
        <div class="stat-badge squat">
          <span class="stat-label">深蹲</span>
          <span class="stat-value">{{ latestStats?.squat || 0 }} kg</span>
        </div>
        <div class="stat-badge bench">
          <span class="stat-label">卧推</span>
          <span class="stat-value">{{ latestStats?.bench || 0 }} kg</span>
        </div>
        <div class="stat-badge deadlift">
          <span class="stat-label">硬拉</span>
          <span class="stat-value">{{ latestStats?.deadlift || 0 }} kg</span>
        </div>
        <div class="stat-badge total">
          <span class="stat-label">总和</span>
          <span class="stat-value">{{ latestStats?.total || 0 }} kg</span>
        </div>
      </div>
    </div>

    <div class="chart-wrapper">
      <Line :data="chartData" :options="chartOptions" />
    </div>

    <div v-if="!hasData" class="empty-chart">
      <div class="empty-icon">📊</div>
      <p>暂无训练数据</p>
      <p class="empty-hint">开始训练后，你的三大项成绩将在这里展示</p>
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

interface BigThreeStats {
  recordDate: string
  squat: number
  bench: number
  deadlift: number
  total: number
}

const statsData = ref<BigThreeStats[]>([])
const hasData = computed(() => statsData.value.length > 0)
const latestStats = computed(() => statsData.value[statsData.value.length - 1])

const chartData = computed(() => {
  if (!hasData.value) {
    return { labels: [], datasets: [] }
  }

  return {
    labels: statsData.value.map(s => new Date(s.recordDate).toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' })),
    datasets: [
      {
        label: '深蹲',
        backgroundColor: 'rgba(255, 107, 122, 0.1)',
        borderColor: '#ff6b7a',
        data: statsData.value.map(s => s.squat),
        tension: 0.4,
        fill: true,
        pointRadius: 4,
        pointHoverRadius: 6,
        pointBackgroundColor: '#ff6b7a',
        pointBorderColor: '#fff',
        pointBorderWidth: 2
      },
      {
        label: '卧推',
        backgroundColor: 'rgba(76, 175, 80, 0.1)',
        borderColor: '#4caf50',
        data: statsData.value.map(s => s.bench),
        tension: 0.4,
        fill: true,
        pointRadius: 4,
        pointHoverRadius: 6,
        pointBackgroundColor: '#4caf50',
        pointBorderColor: '#fff',
        pointBorderWidth: 2
      },
      {
        label: '硬拉',
        backgroundColor: 'rgba(33, 150, 243, 0.1)',
        borderColor: '#2196f3',
        data: statsData.value.map(s => s.deadlift),
        tension: 0.4,
        fill: true,
        pointRadius: 4,
        pointHoverRadius: 6,
        pointBackgroundColor: '#2196f3',
        pointBorderColor: '#fff',
        pointBorderWidth: 2
      },
      {
        label: '总和',
        backgroundColor: 'rgba(156, 39, 176, 0.1)',
        borderColor: '#9c27b0',
        data: statsData.value.map(s => s.total),
        borderWidth: 3,
        tension: 0.4,
        fill: false,
        pointRadius: 5,
        pointHoverRadius: 7,
        pointBackgroundColor: '#9c27b0',
        pointBorderColor: '#fff',
        pointBorderWidth: 2
      }
    ]
  }
})

const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false,
  interaction: {
    mode: 'index',
    intersect: false
  },
  scales: {
    x: {
      type: 'category' as const,
      ticks: {
        color: '#b0b0b0',
        font: { size: 12 }
      },
      grid: {
        color: 'rgba(255, 255, 255, 0.05)'
      }
    },
    y: {
      type: 'linear' as const,
      beginAtZero: true,
      ticks: {
        color: '#b0b0b0',
        font: { size: 12 },
        callback: (value: any) => `${value} kg`
      },
      grid: {
        color: 'rgba(255, 255, 255, 0.05)'
      }
    }
  },
  plugins: {
    legend: {
      position: 'top' as const,
      labels: {
        color: '#e0e0e0',
        font: { size: 13 },
        usePointStyle: true,
        padding: 15
      }
    },
    tooltip: {
      backgroundColor: 'rgba(30, 30, 30, 0.95)',
      titleColor: '#fff',
      bodyColor: '#e0e0e0',
      borderColor: 'rgba(255, 71, 87, 0.3)',
      borderWidth: 1,
      padding: 12,
      displayColors: true,
      callbacks: {
        label: (context: any) => `${context.dataset.label}: ${context.parsed.y} kg`
      }
    }
  }
})

async function fetchChartData() {
  try {
    const response = await fetch('/api/workout/stats/big-three?userId=1')
    if (response.ok) {
      const apiResponse = await response.json()
      statsData.value = apiResponse.data || []
    }
  } catch (error) {
    console.error('获取图表数据失败:', error)
  }
}

onMounted(() => {
  fetchChartData()
})
</script>

<style scoped>
.progress-chart-container {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.05), rgba(255, 255, 255, 0.02));
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 24px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chart-header {
  margin-bottom: 20px;
}

.chart-header h2 {
  margin: 0 0 16px 0;
  font-size: 20px;
  color: var(--text-color);
  font-weight: 600;
}

.chart-stats {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.stat-badge {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  padding: 10px 16px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  transition: all 0.3s ease;
}

.stat-badge:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.stat-badge.squat {
  border-color: rgba(255, 107, 122, 0.3);
}

.stat-badge.squat:hover {
  background: rgba(255, 107, 122, 0.1);
}

.stat-badge.bench {
  border-color: rgba(76, 175, 80, 0.3);
}

.stat-badge.bench:hover {
  background: rgba(76, 175, 80, 0.1);
}

.stat-badge.deadlift {
  border-color: rgba(33, 150, 243, 0.3);
}

.stat-badge.deadlift:hover {
  background: rgba(33, 150, 243, 0.1);
}

.stat-badge.total {
  border-color: rgba(156, 39, 176, 0.3);
  background: rgba(156, 39, 176, 0.1);
}

.stat-label {
  font-size: 12px;
  color: #888;
  font-weight: 500;
}

.stat-value {
  font-size: 16px;
  color: var(--text-color);
  font-weight: 600;
}

.chart-wrapper {
  flex: 1;
  position: relative;
  min-height: 250px;
}

.empty-chart {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-chart p {
  color: #888;
  margin: 8px 0;
}

.empty-hint {
  font-size: 14px;
  color: #666;
}
</style>