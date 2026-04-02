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
header {
  text-align: center;
  margin-bottom: 2rem;
}

main {
  flex: 1;
}

footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: var(--secondary-color);
  padding: 1rem;
}

nav {
  display: flex;
  justify-content: space-around;
}

nav button {
  background: none;
  border: none;
  color: var(--text-color);
  font-size: 1rem;
  cursor: pointer;
}
</style>
