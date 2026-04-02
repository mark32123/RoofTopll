<template>
  <div class="card">
    <h2>RM 计算器</h2>
    <form @submit.prevent="calculateRM">
      <div>
        <label for="weight">重量 (kg)</label>
        <input id="weight" v-model.number="weight" type="number" required>
      </div>
      <div>
        <label for="reps">次数</label>
        <input id="reps" v-model.number="reps" type="number" required>
      </div>
      <button type="submit" class="btn">计算</button>
    </form>
    <div v-if="oneRepMax" class="result">
      <h3>你的 1RM 估计是:</h3>
      <p>{{ oneRepMax }} kg</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const weight = ref<number | null>(null);
const reps = ref<number | null>(null);
const oneRepMax = ref<number | null>(null);

function calculateRM() {
  if (weight.value && reps.value) {
    // Epley formula
    oneRepMax.value = Math.round(weight.value * (1 + reps.value / 30));
  }
}
</script>

<style scoped>
.result {
  margin-top: 2rem;
  text-align: center;
}

.result p {
  font-size: 2rem;
  font-weight: bold;
  color: var(--primary-color);
}
</style>
