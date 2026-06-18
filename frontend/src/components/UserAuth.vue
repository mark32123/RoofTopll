<template>
  <div class="user-auth-container">
    <div class="auth-header">
      <div class="logo-section">
        <div class="logo-icon">🏋️</div>
        <h1>RoofTopLL</h1>
        <p class="logo-subtitle">力量训练追踪平台</p>
      </div>
    </div>

    <div class="auth-tabs">
      <button
        :class="['auth-tab', { active: activeTab === 'login' }]"
        @click="activeTab = 'login'"
      >
        登录
      </button>
      <button
        :class="['auth-tab', { active: activeTab === 'register' }]"
        @click="activeTab = 'register'"
      >
        注册
      </button>
    </div>

    <!-- 登录表单 -->
    <div v-if="activeTab === 'login'" class="auth-form">
      <div class="form-group">
        <label for="login-username">用户名</label>
        <input
          id="login-username"
          v-model="loginForm.username"
          type="text"
          placeholder="输入用户名"
          required
        >
      </div>

      <div class="form-group">
        <label for="login-password">密码</label>
        <input
          id="login-password"
          v-model="loginForm.password"
          type="password"
          placeholder="输入密码"
          required
        >
      </div>

      <button @click="handleLogin" class="btn-auth" :disabled="!loginForm.username || !loginForm.password">
        <span class="btn-icon">🔐</span>
        <span>登录</span>
      </button>

      <div v-if="loginError" class="error-message">
        <span class="error-icon">⚠️</span>
        <span>{{ loginError }}</span>
      </div>
    </div>

    <!-- 注册表单 -->
    <div v-if="activeTab === 'register'" class="auth-form">
      <div class="form-group">
        <label for="register-username">用户名</label>
        <input
          id="register-username"
          v-model="registerForm.username"
          type="text"
          placeholder="设置用户名"
          required
        >
      </div>

      <div class="form-group">
        <label for="register-password">密码</label>
        <input
          id="register-password"
          v-model="registerForm.password"
          type="password"
          placeholder="设置密码"
          required
        >
      </div>

      <div class="form-group">
        <label for="register-gender">性别</label>
        <div class="gender-options">
          <button
            :class="['gender-option', { active: registerForm.gender === 1 }]"
            @click="registerForm.gender = 1"
          >
            <span class="gender-icon">👨</span>
            <span>男</span>
          </button>
          <button
            :class="['gender-option', { active: registerForm.gender === 2 }]"
            @click="registerForm.gender = 2"
          >
            <span class="gender-icon">👩</span>
            <span>女</span>
          </button>
        </div>
      </div>

      <button @click="handleRegister" class="btn-auth" :disabled="!registerForm.username || !registerForm.password">
        <span class="btn-icon">✨</span>
        <span>注册</span>
      </button>

      <div v-if="registerSuccess" class="success-message">
        <span class="success-icon">🎉</span>
        <span>{{ registerSuccess }}</span>
      </div>

      <div v-if="registerError" class="error-message">
        <span class="error-icon">⚠️</span>
        <span>{{ registerError }}</span>
      </div>
    </div>

    <!-- 用户信息展示 -->
    <div v-if="currentUser" class="user-info-section">
      <div class="user-card">
        <div class="user-avatar">
          {{ currentUser.gender === 1 ? '👨' : currentUser.gender === 2 ? '👩' : '👤' }}
        </div>
        <div class="user-details">
          <h3>{{ currentUser.username }}</h3>
          <p class="user-join-date">加入时间: {{ formatDate(currentUser.createTime) }}</p>
        </div>
        <button @click="handleLogout" class="btn-logout">
          <span>退出登录</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface User {
  id: number
  username: string
  gender: number
  createTime: string
}

const activeTab = ref('login')
const currentUser = ref<User | null>(null)
const loginError = ref('')
const registerError = ref('')
const registerSuccess = ref('')

const loginForm = ref({
  username: '',
  password: ''
})

const registerForm = ref({
  username: '',
  password: '',
  gender: 0
})

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
      registerSuccess.value = data.data || '注册成功！'
      registerForm.value = { username: '', password: '', gender: 0 }
      activeTab.value = 'login'
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

onMounted(() => {
  const userId = localStorage.getItem('userId')
  if (userId) {
    // 可以在这里获取用户信息
  }
})
</script>

<style scoped>
.user-auth-container {
  max-width: 400px;
  margin: 0 auto;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.05), rgba(255, 255, 255, 0.02));
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  padding: 32px;
}

.auth-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 48px;
  margin-bottom: 8px;
}

.logo-section h1 {
  margin: 0;
  font-size: 28px;
  color: var(--text-color);
  font-weight: 700;
}

.logo-subtitle {
  margin: 0;
  color: #888;
  font-size: 14px;
}

.auth-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  padding: 4px;
}

.auth-tab {
  flex: 1;
  padding: 12px;
  border-radius: 10px;
  background: transparent;
  border: none;
  color: #888;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.auth-tab:hover {
  color: #e0e0e0;
}

.auth-tab.active {
  background: linear-gradient(135deg, #ff4757, #ff6b7a);
  color: white;
  box-shadow: 0 4px 12px rgba(255, 71, 87, 0.3);
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  color: #e0e0e0;
  font-size: 14px;
  font-weight: 500;
}

.form-group input {
  padding: 14px 16px;
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-color);
  font-size: 16px;
  transition: all 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: rgba(255, 71, 87, 0.5);
  background: rgba(255, 255, 255, 0.08);
  box-shadow: 0 0 0 3px rgba(255, 71, 87, 0.1);
}

.gender-options {
  display: flex;
  gap: 12px;
}

.gender-option {
  flex: 1;
  padding: 12px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #888;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.gender-option:hover {
  background: rgba(255, 255, 255, 0.08);
}

.gender-option.active {
  background: rgba(255, 71, 87, 0.2);
  border-color: rgba(255, 71, 87, 0.5);
  color: #ff6b7a;
}

.gender-icon {
  font-size: 20px;
}

.btn-auth {
  padding: 14px;
  border-radius: 10px;
  background: linear-gradient(135deg, #ff4757, #ff6b7a);
  border: none;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(255, 71, 87, 0.3);
}

.btn-auth:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 71, 87, 0.4);
}

.btn-auth:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-icon {
  font-size: 18px;
}

.error-message {
  padding: 12px 16px;
  background: rgba(255, 71, 87, 0.1);
  border: 1px solid rgba(255, 71, 87, 0.3);
  border-radius: 10px;
  color: #ff6b7a;
  display: flex;
  align-items: center;
  gap: 8px;
}

.success-message {
  padding: 12px 16px;
  background: rgba(76, 175, 80, 0.1);
  border: 1px solid rgba(76, 175, 80, 0.3);
  border-radius: 10px;
  color: #81c784;
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-info-section {
  margin-top: 24px;
}

.user-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-avatar {
  font-size: 40px;
  width: 60px;
  height: 60px;
  background: rgba(255, 71, 87, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-details {
  flex: 1;
}

.user-details h3 {
  margin: 0;
  font-size: 18px;
  color: var(--text-color);
}

.user-join-date {
  margin: 4px 0 0;
  font-size: 12px;
  color: #888;
}

.btn-logout {
  padding: 8px 16px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #888;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-logout:hover {
  background: rgba(255, 71, 87, 0.2);
  color: #ff6b7a;
}
</style>