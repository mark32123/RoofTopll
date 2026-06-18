<template>
  <div class="me-page">
    <!-- 用户信息卡片 -->
    <div class="user-card">
      <div class="user-avatar">
        {{ currentUser?.gender === 1 ? '👨' : currentUser?.gender === 2 ? '👩' : '👤' }}
      </div>
      <div class="user-info">
        <h2 class="user-name">{{ currentUser?.username || '未登录' }}</h2>
        <p class="user-join-date" v-if="currentUser">
          加入时间: {{ formatDate(currentUser.createTime) }}
        </p>
        <button v-else @click="showLogin = true" class="btn-login">
          点击登录
        </button>
      </div>
    </div>

    <!-- 登录/注册弹窗 -->
    <div v-if="showLogin" class="modal-overlay" @click.self="showLogin = false">
      <div class="modal-content">
        <button class="modal-close" @click="showLogin = false">×</button>

        <div class="auth-tabs">
          <button
            :class="['auth-tab', { active: authTab === 'login' }]"
            @click="authTab = 'login'"
          >
            登录
          </button>
          <button
            :class="['auth-tab', { active: authTab === 'register' }]"
            @click="authTab = 'register'"
          >
            注册
          </button>
        </div>

        <!-- 登录表单 -->
        <div v-if="authTab === 'login'" class="auth-form">
          <input v-model="loginForm.username" type="text" placeholder="用户名">
          <input v-model="loginForm.password" type="password" placeholder="密码">
          <button @click="handleLogin" class="btn-auth">登录</button>
          <p v-if="loginError" class="error-msg">{{ loginError }}</p>
        </div>

        <!-- 注册表单 -->
        <div v-if="authTab === 'register'" class="auth-form">
          <input v-model="registerForm.username" type="text" placeholder="用户名">
          <input v-model="registerForm.password" type="password" placeholder="密码">
          <div class="gender-select">
            <button
              :class="['gender-btn', { active: registerForm.gender === 1 }]"
              @click="registerForm.gender = 1"
            >
              👨 男
            </button>
            <button
              :class="['gender-btn', { active: registerForm.gender === 2 }]"
              @click="registerForm.gender = 2"
            >
              👩 女
            </button>
          </div>
          <button @click="handleRegister" class="btn-auth">注册</button>
          <p v-if="registerError" class="error-msg">{{ registerError }}</p>
          <p v-if="registerSuccess" class="success-msg">{{ registerSuccess }}</p>
        </div>
      </div>
    </div>

    <!-- 功能菜单 -->
    <div class="menu-section">
      <div class="menu-title">我的数据</div>

      <div class="menu-grid">
        <!-- RM计算器 -->
        <div class="menu-item" @click="openSection('rm')">
          <div class="menu-icon">📊</div>
          <span class="menu-label">RM计算器</span>
          <span class="menu-arrow">›</span>
        </div>

        <!-- 进步曲线 -->
        <div class="menu-item" @click="openSection('progress')">
          <div class="menu-icon">📈</div>
          <span class="menu-label">进步曲线</span>
          <span class="menu-arrow">›</span>
        </div>

        <!-- 体重追踪 -->
        <div class="menu-item" @click="openSection('weight')">
          <div class="menu-icon">⚖️</div>
          <span class="menu-label">体重追踪</span>
          <span class="menu-arrow">›</span>
        </div>

        <!-- 补剂记录 -->
        <div class="menu-item" @click="openSection('supplement')">
          <div class="menu-icon">💊</div>
          <span class="menu-label">补剂记录</span>
          <span class="menu-arrow">›</span>
        </div>
      </div>
    </div>

    <!-- 展开的详情面板 -->
    <transition name="slide">
      <div v-if="activeSection" class="detail-panel">
        <div class="detail-header">
          <button class="back-btn" @click="activeSection = null">‹ 返回</button>
          <h3>{{ sectionTitle }}</h3>
        </div>

        <div class="detail-content">
          <RMCalculator v-if="activeSection === 'rm'" />
          <ProgressChart v-if="activeSection === 'progress'" />
          <WeightTracker v-if="activeSection === 'weight'" />
          <SupplementTracker v-if="activeSection === 'supplement'" />
        </div>
      </div>
    </transition>

    <!-- 退出登录 -->
    <div v-if="currentUser" class="logout-section">
      <button @click="handleLogout" class="btn-logout">
        退出登录
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import RMCalculator from './RMCalculator.vue'
import ProgressChart from './ProgressChart.vue'
import WeightTracker from './WeightTracker.vue'
import SupplementTracker from './SupplementTracker.vue'

interface User {
  id: number
  username: string
  gender: number
  createTime: string
}

const showLogin = ref(false)
const authTab = ref('login')
const currentUser = ref<User | null>(null)
const activeSection = ref<string | null>(null)

const loginForm = ref({ username: '', password: '' })
const registerForm = ref({ username: '', password: '', gender: 0 })
const loginError = ref('')
const registerError = ref('')
const registerSuccess = ref('')

const sectionTitle = computed(() => {
  const titles: Record<string, string> = {
    rm: 'RM计算器',
    progress: '进步曲线',
    weight: '体重追踪',
    supplement: '补剂记录'
  }
  return titles[activeSection.value || ''] || ''
})

function openSection(section: string) {
  activeSection.value = section
}

async function handleLogin() {
  loginError.value = ''
  try {
    const response = await fetch('/user/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: `username=${loginForm.value.username}&password=${loginForm.value.password}`
    })
    const data = await response.json()
    if (data.code === 200) {
      currentUser.value = data.data
      localStorage.setItem('userId', data.data.id)
      showLogin.value = false
      loginForm.value = { username: '', password: '' }
    } else {
      loginError.value = data.msg || '登录失败'
    }
  } catch (error) {
    loginError.value = '网络请求失败'
  }
}

async function handleRegister() {
  registerError.value = ''
  registerSuccess.value = ''
  try {
    const response = await fetch('/user/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(registerForm.value)
    })
    const data = await response.json()
    if (data.code === 200) {
      registerSuccess.value = '注册成功！'
      registerForm.value = { username: '', password: '', gender: 0 }
      setTimeout(() => {
        authTab.value = 'login'
        registerSuccess.value = ''
      }, 1500)
    } else {
      registerError.value = data.msg || '注册失败'
    }
  } catch (error) {
    registerError.value = '网络请求失败'
  }
}

function handleLogout() {
  currentUser.value = null
  localStorage.removeItem('userId')
}

function formatDate(dateString: string): string {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.me-page {
  padding: 20px;
  padding-bottom: 100px;
}

/* 用户卡片 */
.user-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: linear-gradient(135deg, rgba(255, 71, 87, 0.2), rgba(255, 107, 122, 0.1));
  border: 1px solid rgba(255, 71, 87, 0.3);
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
}

.user-avatar {
  font-size: 56px;
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-info {
  flex: 1;
}

.user-name {
  margin: 0;
  font-size: 24px;
  color: var(--text-color);
  font-weight: 600;
}

.user-join-date {
  margin: 8px 0 0;
  font-size: 14px;
  color: #888;
}

.btn-login {
  margin-top: 8px;
  padding: 10px 24px;
  background: linear-gradient(135deg, #ff4757, #ff6b7a);
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

/* 登录弹窗 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.modal-content {
  background: #1a1a1a;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 32px;
  width: 90%;
  max-width: 400px;
  position: relative;
}

.modal-close {
  position: absolute;
  top: 16px;
  right: 16px;
  background: none;
  border: none;
  color: #888;
  font-size: 28px;
  cursor: pointer;
}

.auth-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
}

.auth-tab {
  flex: 1;
  padding: 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  color: #888;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.auth-tab.active {
  background: linear-gradient(135deg, #ff4757, #ff6b7a);
  border-color: transparent;
  color: white;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.auth-form input {
  padding: 14px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  color: var(--text-color);
  font-size: 16px;
}

.auth-form input:focus {
  outline: none;
  border-color: rgba(255, 71, 87, 0.5);
}

.gender-select {
  display: flex;
  gap: 12px;
}

.gender-btn {
  flex: 1;
  padding: 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  color: #888;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.gender-btn.active {
  background: rgba(255, 71, 87, 0.2);
  border-color: rgba(255, 71, 87, 0.5);
  color: #ff6b7a;
}

.btn-auth {
  padding: 14px;
  background: linear-gradient(135deg, #ff4757, #ff6b7a);
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
}

.error-msg {
  color: #ff6b7a;
  font-size: 14px;
  text-align: center;
}

.success-msg {
  color: #81c784;
  font-size: 14px;
  text-align: center;
}

/* 菜单 */
.menu-section {
  margin-bottom: 24px;
}

.menu-title {
  font-size: 14px;
  color: #888;
  margin-bottom: 12px;
  padding-left: 4px;
}

.menu-grid {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  cursor: pointer;
  transition: all 0.3s;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.05);
}

.menu-item:active {
  background: rgba(255, 71, 87, 0.1);
}

.menu-icon {
  font-size: 24px;
  width: 40px;
  height: 40px;
  background: rgba(255, 71, 87, 0.1);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-label {
  flex: 1;
  font-size: 16px;
  color: var(--text-color);
}

.menu-arrow {
  font-size: 20px;
  color: #888;
}

/* 详情面板 */
.detail-panel {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: var(--bg-color);
  z-index: 1500;
  overflow-y: auto;
}

.detail-header {
  position: sticky;
  top: 0;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  background: rgba(20, 20, 20, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  z-index: 10;
}

.back-btn {
  background: none;
  border: none;
  color: #ff6b7a;
  font-size: 20px;
  cursor: pointer;
  padding: 8px 12px;
  margin-left: -12px;
}

.detail-header h3 {
  margin: 0;
  font-size: 18px;
  color: var(--text-color);
}

.detail-content {
  padding: 20px;
}

/* 动画 */
.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from {
  transform: translateX(100%);
}

.slide-leave-to {
  transform: translateX(100%);
}

/* 退出登录 */
.logout-section {
  margin-top: 32px;
  padding: 0 20px;
}

.btn-logout {
  width: 100%;
  padding: 14px;
  background: rgba(255, 71, 87, 0.1);
  border: 1px solid rgba(255, 71, 87, 0.3);
  border-radius: 12px;
  color: #ff6b7a;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-logout:hover {
  background: rgba(255, 71, 87, 0.2);
}
</style>