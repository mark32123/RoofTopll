<template>
  <header>
    <h1>{{ title }}</h1>
  </header>

  <main>
    <component :is="currentComponent" />
  </main>

  <footer>
    <nav>
      <button @click="setCurrentComponent('WorkoutPlan')">训练计划</button>
      <button @click="setCurrentComponent('RMCalculator')">RM 计算器</button>
      <button @click="setCurrentComponent('ProgressChart')">数据记录</button>
    </nav>
  </footer>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import WorkoutPlan from './components/WorkoutPlan.vue'
import RMCalculator from './components/RMCalculator.vue'
import ProgressChart from './components/ProgressChart.vue'

const title = ref('RoofTopLL Powerlifting')

const components: { [key: string]: any } = {
  WorkoutPlan,
  RMCalculator,
  ProgressChart,
}

const currentTab = ref('WorkoutPlan')

const currentComponent = computed(() => components[currentTab.value])

function setCurrentComponent(tab: string) {
  currentTab.value = tab
}
</script>

<style scoped>
#app-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

header {
  text-align: center;
  padding: 1.5rem 1rem;
  background-color: var(--secondary-color);
  border-bottom: 1px solid #333;
}

main {
  flex: 1;
  overflow-y: auto; /* 让主内容区可以滚动 */
  padding: 1rem;
  padding-bottom: 6rem; /* 避免被底部导航栏遮挡 */
}

footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: var(--secondary-color);
  border-top: 1px solid #333;
  z-index: 1000;
}

nav {
  display: flex;
  justify-content: space-around;
  padding: 0.5rem 0;
}

nav button {
  background: none;
  border: none;
  color: #888; /* 未选中颜色 */
  font-size: 1rem;
  cursor: pointer;
  padding: 0.5rem 1rem;
  border-radius: var(--border-radius);
  transition: all 0.3s ease;
}

nav button.active {
  color: var(--primary-color); /* 选中颜色 */
  font-weight: bold;
}
</style>
