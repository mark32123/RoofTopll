<template>
  <div class="rm-calculator-container">
    <div class="calculator-header">
      <h2>RM 计算器</h2>
      <p class="header-desc">根据重量和次数估算你的单次最大重量（1RM）</p>
    </div>

    <div class="calculator-form">
      <div class="input-group">
        <label for="weight">重量</label>
        <div class="input-wrapper">
          <input
            id="weight"
            v-model.number="weight"
            type="number"
            step="0.1"
            placeholder="输入重量"
            required
          >
          <span class="input-unit">kg</span>
        </div>
      </div>

      <div class="input-group">
        <label for="reps">次数</label>
        <div class="input-wrapper">
          <input
            id="reps"
            v-model.number="reps"
            type="number"
            min="1"
            max="30"
            placeholder="输入次数"
            required
          >
          <span class="input-unit">次</span>
        </div>
      </div>

      <button @click="calculateRM" class="btn-calculate" :disabled="!weight || !reps">
        <span class="btn-icon">💪</span>
        <span>计算 1RM</span>
      </button>
    </div>

    <div v-if="result" class="result-section">
      <div class="result-header">
        <h3>你的 1RM 估算值</h3>
        <div class="result-highlight">
          <span class="highlight-value">{{ result.average }}</span>
          <span class="highlight-unit">kg</span>
        </div>
      </div>

      <div class="result-details">
        <div class="result-card">
          <div class="result-card-header">
            <span class="formula-name">Epley 公式</span>
            <span class="formula-badge">经典</span>
          </div>
          <div class="result-value">{{ result.epley }} kg</div>
          <div class="formula-desc">适合中高强度训练</div>
        </div>

        <div class="result-card">
          <div class="result-card-header">
            <span class="formula-name">Brzycki 公式</span>
            <span class="formula-badge">精准</span>
          </div>
          <div class="result-value">{{ result.brzycki }} kg</div>
          <div class="formula-desc">适合低次数训练</div>
        </div>

        <div class="result-card">
          <div class="result-card-header">
            <span class="formula-name">Lombardi 公式</span>
            <span class="formula-badge">简单</span>
          </div>
          <div class="result-value">{{ result.lombardi }} kg</div>
          <div class="formula-desc">适合快速估算</div>
        </div>
      </div>

      <div class="training-suggestions">
        <h4>训练建议</h4>
        <div class="suggestion-grid">
          <div class="suggestion-item">
            <span class="suggestion-label">90% 1RM</span>
            <span class="suggestion-value">{{ Math.round(result.average * 0.9) }} kg</span>
            <span class="suggestion-desc">力量训练</span>
          </div>
          <div class="suggestion-item">
            <span class="suggestion-label">80% 1RM</span>
            <span class="suggestion-value">{{ Math.round(result.average * 0.8) }} kg</span>
            <span class="suggestion-desc">增肌训练</span>
          </div>
          <div class="suggestion-item">
            <span class="suggestion-label">70% 1RM</span>
            <span class="suggestion-value">{{ Math.round(result.average * 0.7) }} kg</span>
            <span class="suggestion-desc">耐力训练</span>
          </div>
          <div class="suggestion-item">
            <span class="suggestion-label">60% 1RM</span>
            <span class="suggestion-value">{{ Math.round(result.average * 0.6) }} kg</span>
            <span class="suggestion-desc">热身重量</span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="error" class="error-message">
      <span class="error-icon">⚠️</span>
      <span>{{ error }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface RMResult {
  epley: number
  brzycki: number
  lombardi: number
  average: number
}

const weight = ref<number | null>(null)
const reps = ref<number | null>(null)
const result = ref<RMResult | null>(null)
const error = ref<string | null>(null)

async function calculateRM() {
  error.value = null
  result.value = null

  if (!weight.value || !reps.value || reps.value <= 0) {
    error.value = '请输入有效的重量和次数'
    return
  }

  if (reps.value > 30) {
    error.value = '次数超过30次时，估算结果可能不准确'
    return
  }

  try {
    const response = await fetch(`/tool/rm-calc?weight=${weight.value}&reps=${reps.value}`)
    const data = await response.json()

    if (data.code === 200) {
      result.value = data.data
    } else {
      error.value = data.msg || '计算失败'
    }
  } catch (e) {
    error.value = '网络请求失败，请检查后端服务是否启动'
  }
}
</script>

<style scoped>
.rm-calculator-container {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.05), rgba(255, 255, 255, 0.02));
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 24px;
}

.calculator-header {
  text-align: center;
  margin-bottom: 24px;
}

.calculator-header h2 {
  margin: 0;
  font-size: 24px;
  color: var(--text-color);
  font-weight: 600;
}

.header-desc {
  margin: 8px 0 0;
  color: #888;
  font-size: 14px;
}

.calculator-form {
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
  font-weight: 500;
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
  transition: all 0.3s ease;
}

.input-wrapper input:focus {
  outline: none;
  border-color: rgba(255, 71, 87, 0.5);
  background: rgba(255, 255, 255, 0.08);
  box-shadow: 0 0 0 3px rgba(255, 71, 87, 0.1);
}

.input-unit {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #888;
  font-size: 14px;
}

.btn-calculate {
  padding: 12px 24px;
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
  gap: 8px;
  box-shadow: 0 4px 12px rgba(255, 71, 87, 0.3);
}

.btn-calculate:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 71, 87, 0.4);
}

.btn-calculate:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-icon {
  font-size: 20px;
}

.result-section {
  margin-top: 24px;
}

.result-header {
  text-align: center;
  margin-bottom: 20px;
}

.result-header h3 {
  margin: 0 0 12px;
  font-size: 18px;
  color: var(--text-color);
}

.result-highlight {
  display: inline-flex;
  align-items: baseline;
  gap: 8px;
  background: linear-gradient(135deg, rgba(255, 71, 87, 0.2), rgba(255, 71, 87, 0.1));
  border: 2px solid rgba(255, 71, 87, 0.3);
  border-radius: 12px;
  padding: 16px 32px;
}

.highlight-value {
  font-size: 36px;
  font-weight: 700;
  color: #ff6b7a;
}

.highlight-unit {
  font-size: 18px;
  color: #888;
}

.result-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.result-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 16px;
  transition: all 0.3s ease;
}

.result-card:hover {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 71, 87, 0.2);
  transform: translateY(-2px);
}

.result-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.formula-name {
  font-size: 14px;
  color: var(--text-color);
  font-weight: 500;
}

.formula-badge {
  background: rgba(76, 175, 80, 0.2);
  color: #81c784;
  padding: 3px 8px;
  border-radius: 6px;
  font-size: 11px;
}

.result-value {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 8px;
}

.formula-desc {
  font-size: 12px;
  color: #888;
}

.training-suggestions {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px;
}

.training-suggestions h4 {
  margin: 0 0 16px;
  font-size: 16px;
  color: var(--text-color);
}

.suggestion-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 12px;
}

.suggestion-item {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  padding: 12px;
  text-align: center;
  transition: all 0.3s ease;
}

.suggestion-item:hover {
  background: rgba(255, 71, 87, 0.1);
}

.suggestion-label {
  font-size: 12px;
  color: #888;
  display: block;
  margin-bottom: 6px;
}

.suggestion-value {
  font-size: 18px;
  font-weight: 600;
  color: #ff6b7a;
  display: block;
  margin-bottom: 4px;
}

.suggestion-desc {
  font-size: 11px;
  color: #666;
}

.error-message {
  margin-top: 16px;
  padding: 12px 16px;
  background: rgba(255, 71, 87, 0.1);
  border: 1px solid rgba(255, 71, 87, 0.3);
  border-radius: 10px;
  color: #ff6b7a;
  display: flex;
  align-items: center;
  gap: 8px;
}

.error-icon {
  font-size: 18px;
}
</style>