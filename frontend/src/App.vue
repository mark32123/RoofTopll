<template>
  <div id="app-container">
    <header>
      <h1>{{ title }}</h1>
      <p class="subtitle">力量训练追踪系统</p>
    </header>

    <main>
      <component :is="currentComponent" />
    </main>

    <footer>
      <nav ref="navRef">
        <button
          v-for="(tab, index) in tabs"
          :key="tab.key"
          :class="['nav-btn', { active: currentTab === tab.key }]"
          @click="setCurrentComponent(tab.key)"
        >
          <span class="btn-glow"></span>
          <span class="btn-icon">{{ tab.icon }}</span>
          <span class="btn-text">{{ tab.label }}</span>
        </button>
      </nav>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import WorkoutPlan from './components/WorkoutPlan.vue'
import ExerciseLibrary from './components/ExerciseLibrary.vue'
import RMCalculator from './components/RMCalculator.vue'
import ProgressChart from './components/ProgressChart.vue'
import UserAuth from './components/UserAuth.vue'
import WeightTracker from './components/WeightTracker.vue'
import SupplementTracker from './components/SupplementTracker.vue'

const title = ref('RoofTopLL Powerlifting')
const navRef = ref<HTMLElement | null>(null)

const tabs = [
  { key: 'WorkoutPlan', label: '训练计划', icon: '💪' },
  { key: 'ExerciseLibrary', label: '动作库', icon: '🏋️' },
  { key: 'RMCalculator', label: 'RM计算', icon: '📊' },
  { key: 'ProgressChart', label: '进步曲线', icon: '📈' },
  { key: 'WeightTracker', label: '体重', icon: '⚖️' },
  { key: 'SupplementTracker', label: '补剂', icon: '💊' },
  { key: 'UserAuth', label: '用户', icon: '👤' }
]

const components: { [key: string]: any } = {
  WorkoutPlan,
  ExerciseLibrary,
  RMCalculator,
  ProgressChart,
  WeightTracker,
  SupplementTracker,
  UserAuth
}

const currentTab = ref('WorkoutPlan')
const currentComponent = computed(() => components[currentTab.value])

function setCurrentComponent(tab: string) {
  currentTab.value = tab
}

onMounted(() => {
  if (navRef.value) {
    const buttons = navRef.value.querySelectorAll('button')

    buttons.forEach((btn) => {
      btn.addEventListener('mousemove', (e) => {
        const rect = btn.getBoundingClientRect()
        const x = e.clientX - rect.left
        const y = e.clientY - rect.top

        const glow = btn.querySelector('.btn-glow') as HTMLElement
        if (glow) {
          glow.style.setProperty('--mouse-x', `${x}px`)
          glow.style.setProperty('--mouse-y', `${y}px`)
        }
      })
    })
  }
})
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
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

header h1 {
  margin: 0;
  font-size: 28px;
  color: var(--text-color);
  font-weight: 700;
}

.subtitle {
  margin: 8px 0 0;
  color: #888;
  font-size: 14px;
}

main {
  flex: 1;
  overflow-y: auto;
  padding: 1rem;
  padding-bottom: 8rem;
}

footer {
  position: fixed;
  bottom: 24px;
  left: 0;
  right: 0;
  z-index: 1000;
  padding: 0 20px;
  pointer-events: none;
}

nav {
  max-width: 600px;
  margin: 0 auto;
  display: flex;
  gap: 6px;
  justify-content: space-around;
  background: rgba(20, 20, 20, 0.92);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  padding: 8px 12px;
  border-radius: 50px;
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow:
    0 12px 40px rgba(0, 0, 0, 0.6),
    0 4px 16px rgba(0, 0, 0, 0.4),
    inset 0 1px 1px rgba(255, 255, 255, 0.1);
  pointer-events: auto;
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
}

nav:hover {
  background: rgba(25, 25, 25, 0.95);
  border-color: rgba(255, 255, 255, 0.12);
  box-shadow:
    0 16px 52px rgba(0, 0, 0, 0.7),
    0 6px 20px rgba(0, 0, 0, 0.5),
    inset 0 1px 1px rgba(255, 255, 255, 0.15);
  transform: scale(1.02);
}

.nav-btn {
  background: rgba(255, 255, 255, 0.05);
  border: none;
  color: #b0b0b0;
  font-size: 0.85rem;
  font-weight: 500;
  cursor: pointer;
  padding: 10px 14px;
  border-radius: 30px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  flex: 1;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.btn-text {
  position: relative;
  z-index: 2;
}

.btn-icon {
  font-size: 16px;
  z-index: 2;
}

.btn-glow {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: 30px;
  background: radial-gradient(
    circle at var(--mouse-x, 50%) var(--mouse-y, 50%),
    rgba(255, 71, 87, 0.8) 0%,
    rgba(255, 107, 122, 0.5) 15%,
    rgba(255, 143, 163, 0.3) 30%,
    transparent 60%
  );
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: 1;
  pointer-events: none;
  filter: blur(2px);
}

.nav-btn:hover .btn-glow {
  opacity: 1;
  animation: neon-pulse 1.5s ease-in-out infinite;
}

@keyframes neon-pulse {
  0%, 100% {
    box-shadow:
      0 0 10px rgba(255, 71, 87, 0.5),
      0 0 20px rgba(255, 71, 87, 0.3),
      0 0 30px rgba(255, 71, 87, 0.2);
  }
  50% {
    box-shadow:
      0 0 20px rgba(255, 71, 87, 0.8),
      0 0 40px rgba(255, 71, 87, 0.5),
      0 0 60px rgba(255, 71, 87, 0.3);
  }
}

.nav-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  transition: left 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.nav-btn:hover::before {
  left: 100%;
}

.nav-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #e0e0e0;
  transform: translateY(-2px) scale(1.05);
}

.nav-btn.active {
  background: linear-gradient(135deg, #ff4757, #ff6b7a);
  color: white;
  font-weight: 600;
  box-shadow:
    0 4px 16px rgba(255, 71, 87, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.nav-btn.active:hover {
  background: linear-gradient(135deg, #ff6b7a, #ff8fa3);
  box-shadow:
    0 6px 24px rgba(255, 71, 87, 0.5),
    0 0 40px rgba(255, 71, 87, 0.3),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
  transform: translateY(-2px) scale(1.08);
}
</style>