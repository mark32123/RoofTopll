<template>
  <div class="card">
    <h2>训练计划</h2>
    <button class="btn" @click="openCreateModal">新建计划</button>
    <ul>
      <li v-for="plan in plans" :key="plan.id">
        <span>{{ plan.name }}</span>
        <div>
          <button @click="openEditModal(plan)">编辑</button>
          <button @click="deletePlan(plan.id)">删除</button>
        </div>
      </li>
    </ul>
  </div>

  <Modal :show="showModal" @close="showModal = false">
    <h3>{{ isEditing ? '编辑计划' : '新建计划' }}</h3>
    <form @submit.prevent="savePlan">
      <div>
        <label for="plan-name">计划名称</label>
        <input id="plan-name" v-model="editablePlan.name" type="text" required>
      </div>
      <div>
        <label for="plan-desc">描述</label>
        <textarea id="plan-desc" v-model="editablePlan.description"></textarea>
      </div>
      <button type="submit" class="btn">保存</button>
    </form>
  </Modal>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Modal from './Modal.vue'

const plans = ref<any[]>([])
const showModal = ref(false)
const isEditing = ref(false)
const editablePlan = ref<any>({})

async function fetchPlans() {
  try {
    const response = await fetch('/api/workout/plan?userId=1');
    if (response.ok) {
      const data = await response.json();
      plans.value = data.data || [];
    } else {
      console.error('获取训练计划失败');
    }
  } catch (error) {
    console.error('请求后端API时出错:', error);
  }
}

function openCreateModal() {
  isEditing.value = false;
  editablePlan.value = { name: '', description: '' };
  showModal.value = true;
}

function openEditModal(plan: any) {
  isEditing.value = true;
  editablePlan.value = { ...plan };
  showModal.value = true;
}

async function savePlan() {
  const method = isEditing.value ? 'PUT' : 'POST';
  const url = isEditing.value ? `/api/workout/plan/${editablePlan.value.id}` : '/api/workout/plan';
  editablePlan.value.userId = 1; // 假设用户ID为1

  try {
    const response = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(editablePlan.value),
    });

    if (response.ok) {
      showModal.value = false;
      fetchPlans(); // 重新获取列表
    } else {
      console.error('保存失败');
    }
  } catch (error) {
    console.error('请求后端API时出错:', error);
  }
}

async function deletePlan(planId: number) {
  if (!confirm('确定要删除这个计划吗？')) return;

  try {
    const response = await fetch(`/api/workout/plan/${planId}`, { method: 'DELETE' });
    if (response.ok) {
      fetchPlans(); // 重新获取列表
    } else {
      console.error('删除失败');
    }
  } catch (error) {
    console.error('请求后端API时出错:', error);
  }
}

onMounted(() => {
  fetchPlans();
});
</script>

<style scoped>
/* ... 之前的样式保持不变 ... */
form div {
  margin-bottom: 1rem;
}
label {
  display: block;
  margin-bottom: 0.5rem;
}
input, textarea {
  width: 100%;
  padding: 0.5rem;
  border-radius: var(--border-radius);
  border: 1px solid #555;
  background-color: #333;
  color: var(--text-color);
}
</style>
