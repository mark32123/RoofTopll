<template>
  <div class="card">
    <h2>进步曲线</h2>
    <Line :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js'

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
)

const chartData = ref<any>({ labels: [], datasets: [] });
const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false,
  scales: {
    y: {
      beginAtZero: true
    }
  }
});

async function fetchChartData() {
  try {
    const response = await fetch('/api/workout/stats/big-three?userId=1'); // 假设用户ID为1
    if (response.ok) {
      const apiResponse = await response.json();
      const stats = apiResponse.data;

      chartData.value = {
        labels: stats.map((s: any) => new Date(s.date).toLocaleDateString()),
        datasets: [
          {
            label: '三大项总和 (kg)',
            backgroundColor: '#f87979',
            borderColor: '#f87979',
            data: stats.map((s: any) => s.totalWeight)
          }
        ]
      };
    } else {
      console.error('获取图表数据失败');
    }
  } catch (error) {
    console.error('请求后端API时出错:', error);
  }
}

onMounted(() => {
  fetchChartData();
});
</script>
