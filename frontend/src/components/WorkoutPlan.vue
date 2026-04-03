x<template>
  <div class="workout-plans-container">
    <!-- 新建计划按钮 -->
    <div class="create-plan-section">
      <button class="btn btn-primary btn-create" @click="openCreateModal">
        <span class="btn-icon">+</span> 新建计划
      </button>
    </div>

    <!-- 今日训练计划 -->
    <div class="plans-section">
      <h2 class="section-title">今日训练计划</h2>
      <div v-if="todayPlans.length > 0" class="plans-grid">
        <div 
          v-for="plan in todayPlans" 
          :key="plan.id" 
          class="plan-card plan-card-today"
          @click="viewPlanDetail(plan)"
        >
          <div class="plan-card-header">
            <h3 class="plan-card-title">{{ plan.name }}</h3>
            <div class="plan-card-actions">
              <button 
                class="btn-icon-action" 
                @click.stop="openEditModal(plan)"
                title="编辑"
              >
                编辑
              </button>
              <button 
                class="btn-icon-action" 
                @click.stop="deletePlan(plan.id)"
                title="删除"
              >
                删除
              </button>
            </div>
          </div>
          <p class="plan-card-desc">{{ plan.description || '暂无描述' }}</p>
          <div class="plan-card-stats">
            <span class="stat-item">
              {{ plan.activities?.length || 0 }} 个动作
            </span>
            <span class="stat-item">
              {{ getTotalSets(plan) }} 组
            </span>
          </div>
          <div class="plan-card-preview">
            <span 
              v-for="(activity, index) in (plan.activities || []).slice(0, 3)" 
              :key="index"
              class="preview-tag"
            >
              {{ getExerciseName(activity.exerciseId) }}
            </span>
            <span 
              v-if="(plan.activities || []).length > 3" 
              class="preview-tag more"
            >
              +{{ plan.activities.length - 3 }}
            </span>
          </div>
        </div>
      </div>
      <div v-else class="empty-state">
        <div class="empty-icon"></div>
        <p>今天还没有训练计划</p>
        <p class="empty-hint">点击上方"新建计划"开始训练吧！</p>
      </div>
    </div>

    <!-- 历史训练计划 -->
    <div class="plans-section">
      <div class="section-header-with-link">
        <h2 class="section-title">历史训练计划</h2>
        <button 
          v-if="historyPlans.length > 2" 
          class="btn btn-sm btn-link" 
          @click="showHistoryView = true"
        >
          查看全部 →
        </button>
      </div>
      <div v-if="historyPlans.length > 0" class="plans-list-compact">
        <div 
          v-for="plan in historyPlans.slice(0, 2)" 
          :key="plan.id" 
          class="plan-item-compact"
          @click="viewPlanDetail(plan)"
        >
          <div class="plan-item-main">
            <div class="plan-item-icon"></div>
            <div class="plan-item-content">
              <h4 class="plan-item-title">{{ plan.name }}</h4>
              <p class="plan-item-desc">{{ plan.description || '暂无描述' }}</p>
              <div class="plan-item-meta">
                <span class="meta-tag">{{ plan.activities?.length || 0 }} 个动作</span>
                <span class="meta-tag">{{ getTotalSets(plan) }} 组</span>
                <span class="meta-tag date-tag">{{ formatDate(plan.createdAt) }}</span>
              </div>
            </div>
          </div>
          <div class="plan-item-actions">
            <button 
              class="btn btn-sm btn-outline" 
              @click.stop="openEditModal(plan)"
            >
              编辑
            </button>
            <button 
              class="btn btn-sm btn-danger-outline" 
              @click.stop="deletePlan(plan.id)"
            >
              删除
            </button>
          </div>
        </div>
      </div>
      <div v-else class="empty-state">
        <div class="empty-icon"></div>
        <p>还没有历史训练计划</p>
      </div>
    </div>

    <!-- 训练计划模板 -->
    <div class="plans-section">
      <h2 class="section-title">推荐训练模板</h2>
      <div class="templates-grid">
        <div 
          v-for="template in templates" 
          :key="template.id" 
          class="template-card"
          @click="useTemplate(template)"
        >
          <div class="template-card-header">
            <h3 class="template-card-title">{{ template.name }}</h3>
            <span class="template-badge">{{ template.difficulty }}</span>
          </div>
          <p class="template-card-desc">{{ template.description }}</p>
          <div class="template-card-exercises">
            <span 
              v-for="(exercise, index) in template.exercises" 
              :key="index"
              class="exercise-tag"
            >
              {{ exercise }}
            </span>
          </div>
          <button class="btn btn-sm btn-template">使用此模板</button>
        </div>
      </div>
    </div>

    <!-- 历史计划查看全部弹窗 -->
    <Modal :show="showHistoryView" @close="showHistoryView = false">
      <div class="history-view-modal">
        <div class="modal-header">
          <h3>历史训练计划</h3>
          <button class="btn-icon-close" @click="showHistoryView = false">×</button>
        </div>
        
        <div v-if="historyPlans.length > 0" class="history-list">
          <div 
            v-for="plan in historyPlans" 
            :key="plan.id" 
            class="history-item"
          >
            <div class="history-item-content" @click="viewPlanDetail(plan)">
              <h4 class="history-item-title">{{ plan.name }}</h4>
              <p class="history-item-desc">{{ plan.description || '暂无描述' }}</p>
              <div class="history-item-meta">
                <span class="meta-tag">{{ plan.activities?.length || 0 }} 个动作</span>
                <span class="meta-tag">{{ getTotalSets(plan) }} 组</span>
                <span class="meta-tag date-tag">{{ formatDate(plan.createdAt) }}</span>
              </div>
            </div>
            <div class="history-item-actions">
              <button 
                class="btn btn-sm btn-outline" 
                @click.stop="openEditModal(plan)"
              >
                编辑
              </button>
              <button 
                class="btn btn-sm btn-danger-outline" 
                @click.stop="deletePlan(plan.id)"
              >
                删除
              </button>
            </div>
          </div>
        </div>
        <div v-else class="empty-state-small">
          <p>暂无历史训练计划</p>
        </div>
      </div>
    </Modal>

    <!-- 计划详情弹窗 -->
    <Modal :show="showDetailModal" @close="showDetailModal = false">
      <div class="plan-detail-modal">
        <h3>{{ selectedPlan?.name }}</h3>
        <p class="plan-detail-desc">{{ selectedPlan?.description }}</p>
        
        <div v-if="selectedPlan?.activities && selectedPlan.activities.length > 0" class="plan-detail-activities">
          <h4>训练内容</h4>
          <div v-for="(activity, index) in selectedPlan.activities" :key="index" class="detail-activity">
            <div class="detail-activity-header">
              <span class="activity-number">{{ index + 1 }}</span>
              <span class="activity-name">{{ getExerciseName(activity.exerciseId) }}</span>
            </div>
            <div class="detail-activity-sets" v-if="activity.sets && activity.sets.length > 0">
              <div v-for="(set, setIndex) in activity.sets" :key="setIndex" class="detail-set">
                <span class="set-badge">第{{ setIndex + 1 }}组</span>
                <span class="set-detail">{{ set.weight }}kg × {{ set.reps }}次</span>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="empty-state-small">
          <p>暂无训练内容</p>
        </div>
        
        <div class="modal-actions">
          <button class="btn btn-secondary" @click="showDetailModal = false">关闭</button>
          <button class="btn btn-primary" @click="startPlan(selectedPlan)">开始训练</button>
        </div>
      </div>
    </Modal>

    <!-- 新建/编辑计划弹窗 -->
    <Modal :show="showModal" @close="showModal = false">
      <h3>{{ isEditing ? '编辑计划' : '新建计划' }}</h3>
      <form @submit.prevent="savePlan">
        <div class="form-group">
          <label for="plan-name">计划名称</label>
          <input id="plan-name" v-model="editablePlan.name" type="text" required placeholder="例如：周一腿部训练">
        </div>
        <div class="form-group">
          <label for="plan-desc">描述</label>
          <textarea id="plan-desc" v-model="editablePlan.description" placeholder="简要描述训练重点..."></textarea>
        </div>
        
        <!-- 动作列表 -->
        <div class="activities-section">
          <div class="section-header">
            <h4>训练动作</h4>
            <button type="button" class="btn btn-sm btn-outline" @click="addActivity">+ 添加动作</button>
          </div>
          
          <div class="activities-scroll-container" @wheel.stop @touchmove.stop>
            <div v-for="(activity, actIndex) in editablePlan.activities" :key="actIndex" class="activity-item">
              <div class="activity-header">
                <select v-model="activity.exerciseId" required class="exercise-select">
                  <option value="" disabled>选择动作</option>
                  <option v-for="exercise in exercises" :key="exercise.id" :value="exercise.id">
                    {{ exercise.name }} ({{ exercise.category }})
                  </option>
                </select>
                <button type="button" class="btn-icon-remove" @click="removeActivity(actIndex)">×</button>
              </div>
              
              <!-- 组数设置 -->
              <div class="sets-section">
                <div v-for="(set, setIndex) in activity.sets" :key="setIndex" class="set-item">
                  <span class="set-label">第{{ setIndex + 1 }}组</span>
                  <input 
                    type="number" 
                    v-model.number="set.weight" 
                    placeholder="kg" 
                    step="0.5" 
                    min="0"
                    class="set-input"
                  >
                  <span class="set-separator">×</span>
                  <input 
                    type="number" 
                    v-model.number="set.reps" 
                    placeholder="次" 
                    min="1"
                    class="set-input reps"
                  >
                  <button 
                    type="button" 
                    class="btn-icon-remove" 
                    @click="removeSet(actIndex, setIndex)"
                  >
                    ×
                  </button>
                </div>
                
                <button 
                  type="button" 
                  class="btn btn-sm btn-outline btn-add-set" 
                  @click="addSet(actIndex)"
                >
                  + 添加组数
                </button>
              </div>
            </div>
          </div>
        </div>
        
        <div class="form-actions">
          <button type="button" class="btn btn-secondary" @click="showModal = false">取消</button>
          <button type="submit" class="btn btn-primary">{{ isEditing ? '保存修改' : '创建计划' }}</button>
        </div>
      </form>
    </Modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import Modal from './Modal.vue'

const plans = ref<any[]>([])
const showModal = ref(false)
const showDetailModal = ref(false)
const showHistoryView = ref(false)
const isEditing = ref(false)
const editablePlan = ref<any>({ activities: [] })
const selectedPlan = ref<any>(null)
const exercises = ref<any[]>([])

// 预设的训练模板
const templates = ref([
  {
    id: 1,
    name: '新手全身训练',
    description: '适合新手的全身综合性训练',
    difficulty: '初级',
    exercises: ['深蹲', '卧推', '硬拉', '杠铃划船']
  },
  {
    id: 2,
    name: '三大项专项',
    description: '专注提升深蹲、卧推、硬拉成绩',
    difficulty: '中级',
    exercises: ['深蹲', '卧推', '硬拉']
  },
  {
    id: 3,
    name: '腿部轰炸',
    description: '高强度腿部训练',
    difficulty: '高级',
    exercises: ['深蹲', '腿举', '腿弯举', '箭步蹲']
  },
  {
    id: 4,
    name: '推日训练',
    description: '胸、肩、三头肌训练',
    difficulty: '中级',
    exercises: ['卧推', '过头推举', '双杠臂屈伸', '哑铃飞鸟']
  },
  {
    id: 5,
    name: '拉日训练',
    description: '背、二头肌训练',
    difficulty: '中级',
    exercises: ['硬拉', '引体向上', '杠铃划船', '二头弯举']
  }
])

// 计算属性：区分今日计划和历史计划
const todayPlans = computed(() => {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  
  return plans.value.filter(plan => {
    if (!plan.createdAt) return false;
    const planDate = new Date(plan.createdAt);
    planDate.setHours(0, 0, 0, 0);
    return planDate.getTime() === today.getTime();
  });
});

const historyPlans = computed(() => {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  
  return plans.value.filter(plan => {
    if (!plan.createdAt) return true;
    const planDate = new Date(plan.createdAt);
    planDate.setHours(0, 0, 0, 0);
    return planDate.getTime() < today.getTime();
  });
});

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
    console.error('请求后端 API 时出错:', error);
  }
}

async function fetchExercises() {
  try {
    const response = await fetch('/api/exercises/list?userId=0');
    if (response.ok) {
      const data = await response.json();
      exercises.value = data.data || [];
    }
  } catch (error) {
    console.error('获取动作列表失败:', error);
  }
}

function getExerciseName(exerciseId: number): string {
  const exercise = exercises.value.find(e => e.id === exerciseId);
  return exercise ? exercise.name : '未知动作';
}

function getTotalSets(plan: any): number {
  if (!plan.activities) return 0;
  return plan.activities.reduce((total: number, activity: any) => {
    return total + (activity.sets?.length || 0);
  }, 0);
}

function formatDate(dateString: string): string {
  if (!dateString) return '';
  const date = new Date(dateString);
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  const days = Math.floor(diff / (1000 * 60 * 60 * 24));
  
  if (days === 0) return '今天';
  if (days === 1) return '昨天';
  if (days < 7) return `${days}天前`;
  if (days < 30) return `${Math.floor(days / 7)}周前`;
  
  const month = date.getMonth() + 1;
  const day = date.getDate();
  return `${month}月${day}日`;
}

function viewPlanDetail(plan: any) {
  selectedPlan.value = plan;
  showDetailModal.value = true;
}

function startPlan(plan: any) {
  // TODO: 实现开始训练功能
  alert('开始训练：' + plan.name);
  showDetailModal.value = false;
}

function useTemplate(template: any) {
  // 根据模板创建计划
  const templateExercises = template.exercises.map((exName: string) => {
    const exercise = exercises.value.find(e => e.name === exName);
    return {
      exerciseId: exercise ? exercise.id : null,
      sets: [
        { weight: 20, reps: 12 },
        { weight: 30, reps: 10 },
        { weight: 40, reps: 8 }
      ]
    };
  }).filter((a: any) => a.exerciseId !== null);

  editablePlan.value = {
    name: template.name,
    description: template.description,
    userId: 1,
    activities: templateExercises
  };
  isEditing.value = false;
  showModal.value = true;
}

function openCreateModal() {
  isEditing.value = false;
  editablePlan.value = { 
    name: '', 
    description: '', 
    userId: 1,
    activities: [] 
  };
  showModal.value = true;
}

function openEditModal(plan: any) {
  isEditing.value = true;
  // 确保 activities 存在
  if (!plan.activities) {
    plan.activities = [];
  }
  editablePlan.value = { ...plan };
  showModal.value = true;
}

async function savePlan() {
  const method = isEditing.value ? 'PUT' : 'POST';
  const url = isEditing.value ? `/api/workout/plan/${editablePlan.value.id}` : '/api/workout/plan';
  
  // 确保 userId 存在
  if (!editablePlan.value.userId) {
    editablePlan.value.userId = 1;
  }
  
  // 确保 activities 存在
  if (!editablePlan.value.activities) {
    editablePlan.value.activities = [];
  }

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
    console.error('请求后端 API 时出错:', error);
  }
}

function addActivity() {
  editablePlan.value.activities.push({
    exerciseId: null,
    sets: []
  });
}

function removeActivity(index: number) {
  editablePlan.value.activities.splice(index, 1);
}

function addSet(activityIndex: number) {
  editablePlan.value.activities[activityIndex].sets.push({
    weight: null,
    reps: null
  });
}

function removeSet(activityIndex: number, setIndex: number) {
  editablePlan.value.activities[activityIndex].sets.splice(setIndex, 1);
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
    console.error('请求后端 API 时出错:', error);
  }
}

onMounted(() => {
  fetchPlans();
  fetchExercises();
});
</script>

<style scoped>
.workout-plans-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 新建计划区域 */
.create-plan-section {
  margin-bottom: 30px;
}

.btn-create {
  padding: 12px 24px;
  font-size: 16px;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.btn-create:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

.btn-icon {
  font-size: 20px;
  line-height: 1;
}

/* 区域标题 */
.plans-section {
  margin-bottom: 40px;
}

.section-header-with-link {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
  color: var(--text-color);
  border-left: 4px solid #ff4757;
  padding-left: 12px;
}

.btn-link {
  background: transparent;
  color: #ff6b7a;
  border: none;
  padding: 6px 12px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
  border-radius: 6px;
}

.btn-link:hover {
  background: rgba(255, 71, 87, 0.1);
}

/* 今日计划卡片网格 */
.plans-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.plan-card {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.05), rgba(255, 255, 255, 0.02));
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px 20px 15px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 8px;
}

.plan-card:hover {
  transform: translateY(-4px);
  border-color: rgba(255, 71, 87, 0.3);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

.plan-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.plan-card-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: var(--text-color);
}

.plan-card-actions {
  display: flex;
  gap: 8px;
}

.btn-icon-action {
  background: rgba(255, 71, 87, 0.15);
  border: 1px solid rgba(255, 71, 87, 0.3);
  border-radius: 6px;
  width: auto;
  height: 32px;
  padding: 0 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 13px;
  color: #ff6b7a;
}

.btn-icon-action:hover {
  background: rgba(255, 71, 87, 0.25);
  border-color: rgba(255, 71, 87, 0.5);
  transform: scale(1.05);
}

.plan-card-desc {
  color: #aaa;
  font-size: 14px;
  margin-bottom: 15px;
  line-height: 1.5;
}

.plan-card-stats {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #888;
}

.stat-icon {
  font-size: 16px;
}

.plan-card-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.preview-tag {
  background: rgba(255, 71, 87, 0.15);
  color: #ff6b7a;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
}

.preview-tag.more {
  background: rgba(100, 100, 100, 0.3);
  color: #ccc;
}

/* 历史计划列表 */
.plans-list-compact {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.plan-item-compact {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.05), rgba(255, 255, 255, 0.02));
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 18px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.plan-item-compact:hover {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.08), rgba(255, 255, 255, 0.03));
  border-color: rgba(255, 71, 87, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.plan-item-main {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
}

.plan-item-icon {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, rgba(255, 71, 87, 0.2), rgba(255, 71, 87, 0.1));
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
  border: 1px solid rgba(255, 71, 87, 0.2);
}

.plan-item-icon::before {
  content: '🏋️';
}

.plan-item-content {
  flex: 1;
}

.plan-item-title {
  font-size: 17px;
  font-weight: 600;
  margin: 0 0 6px 0;
  color: var(--text-color);
  letter-spacing: 0.3px;
}

.plan-item-desc {
  font-size: 13px;
  color: #888;
  margin: 0 0 10px 0;
  line-height: 1.5;
}

.plan-item-meta {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.meta-tag {
  background: rgba(100, 100, 100, 0.25);
  color: #bbb;
  padding: 4px 10px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.2s ease;
  border: 1px solid rgba(255, 255, 255, 0.08);
}

.meta-tag:hover {
  background: rgba(100, 100, 100, 0.35);
  border-color: rgba(255, 255, 255, 0.15);
}

.meta-tag.date-tag {
  background: linear-gradient(135deg, rgba(255, 71, 87, 0.2), rgba(255, 71, 87, 0.1));
  color: #ff6b7a;
  border-color: rgba(255, 71, 87, 0.3);
}

.meta-tag.date-tag:hover {
  background: linear-gradient(135deg, rgba(255, 71, 87, 0.3), rgba(255, 71, 87, 0.15));
  border-color: rgba(255, 71, 87, 0.4);
}

.plan-item-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  margin-left: 16px;
}

/* 模板卡片 */
.templates-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.template-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.template-card:hover {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(76, 175, 80, 0.3);
  transform: translateY(-4px);
}

.template-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.template-card-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  color: var(--text-color);
}

.template-badge {
  background: rgba(76, 175, 80, 0.2);
  color: #4caf50;
  padding: 3px 10px;
  border-radius: 12px;
  font-size: 12px;
}

.template-card-desc {
  font-size: 13px;
  color: #888;
  margin-bottom: 12px;
}

.template-card-exercises {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 15px;
}

.exercise-tag {
  background: rgba(76, 175, 80, 0.15);
  color: #81c784;
  padding: 4px 10px;
  border-radius: 8px;
  font-size: 12px;
}

.btn-template {
  width: 100%;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 12px;
  border: 2px dashed rgba(255, 255, 255, 0.1);
}

.empty-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto 15px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
}

.empty-state p {
  color: #888;
  margin: 8px 0;
}

.empty-hint {
  font-size: 14px;
  color: #666;
}

.empty-state-small {
  text-align: center;
  padding: 20px;
  color: #888;
}

/* 按钮样式优化 */
.btn {
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.btn-sm {
  padding: 6px 12px;
  font-size: 13px;
}

.btn-primary {
  background: linear-gradient(135deg, #ff4757, #ff6b7a);
  color: white;
  box-shadow: 0 2px 4px rgba(255, 71, 87, 0.3);
}

.btn-primary:hover {
  background: linear-gradient(135deg, #ff6b7a, #ff8fa3);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(255, 71, 87, 0.4);
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.1);
  color: var(--text-color);
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.15);
}

.btn-outline {
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: var(--text-color);
}

.btn-outline:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.5);
}

.btn-danger-outline {
  background: transparent;
  border: 1px solid rgba(255, 71, 87, 0.3);
  color: #ff6b7a;
}

.btn-danger-outline:hover {
  background: rgba(255, 71, 87, 0.1);
  border-color: rgba(255, 71, 87, 0.5);
}

.btn-icon-remove {
  background: rgba(255, 71, 87, 0.2);
  border: none;
  border-radius: 6px;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #ff6b7a;
  font-size: 18px;
}

.btn-icon-remove:hover {
  background: rgba(255, 71, 87, 0.3);
  transform: scale(1.1);
}

/* 表单样式 */
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-color);
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-color);
  font-size: 14px;
}

.form-group textarea {
  min-height: 80px;
  resize: vertical;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 动作区域样式 */
.activities-section {
  margin: 20px 0;
  padding: 15px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 10px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.section-header h4 {
  margin: 0;
  font-size: 16px;
  color: var(--text-color);
}

/* 滚动容器 */
.activities-scroll-container {
  max-height: 280px;
  overflow-y: auto;
  padding-right: 8px;
  margin-top: 15px;
}

.activities-scroll-container::-webkit-scrollbar {
  width: 6px;
}

.activities-scroll-container::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.03);
  border-radius: 3px;
}

.activities-scroll-container::-webkit-scrollbar-thumb {
  background: rgba(255, 71, 87, 0.3);
  border-radius: 3px;
}

.activities-scroll-container::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 71, 87, 0.5);
}

.activity-item {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
}

.activity-header {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 12px;
}

.exercise-select {
  flex: 1;
  padding: 8px;
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-color);
  font-size: 14px;
}

.exercise-select option {
  background: #2a2a2a;
  color: var(--text-color);
}

.sets-section {
  margin-top: 10px;
  padding-left: 10px;
  border-left: 2px solid rgba(255, 255, 255, 0.1);
}

.set-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.set-label {
  min-width: 70px;
  font-size: 13px;
  color: #aaa;
  font-weight: 500;
}

.set-input {
  width: 70px;
  padding: 6px;
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-color);
  font-size: 13px;
  text-align: center;
}

.set-input.reps {
  width: 60px;
}

.set-separator {
  color: #666;
  font-weight: bold;
}

.btn-add-set {
  margin-top: 8px;
}

/* 计划详情弹窗 */
.plan-detail-modal {
  max-width: 600px;
}

.plan-detail-modal h3 {
  margin-bottom: 10px;
}

.plan-detail-desc {
  color: #888;
  margin-bottom: 20px;
}

.plan-detail-activities h4 {
  margin-bottom: 15px;
  color: var(--text-color);
}

.detail-activity {
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 10px;
}

.detail-activity-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.activity-number {
  background: rgba(255, 71, 87, 0.2);
  color: #ff6b7a;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 14px;
}

.activity-name {
  font-weight: 600;
  font-size: 15px;
  color: var(--text-color);
}

.detail-activity-sets {
  padding-left: 38px;
}

.detail-set {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 0;
}

.set-badge {
  background: rgba(100, 100, 100, 0.3);
  color: #aaa;
  padding: 3px 8px;
  border-radius: 6px;
  font-size: 12px;
}

.set-detail {
  color: var(--text-color);
  font-size: 14px;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 历史计划查看全部弹窗 */
.history-view-modal {
  max-width: 800px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.modal-header h3 {
  margin: 0;
  font-size: 20px;
  color: var(--text-color);
}

.btn-icon-close {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 6px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  color: var(--text-color);
  font-size: 24px;
}

.btn-icon-close:hover {
  background: rgba(255, 71, 87, 0.2);
  color: #ff6b7a;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 500px;
  overflow-y: auto;
}

.history-item {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 10px;
  padding: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.2s ease;
}

.history-item:hover {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 71, 87, 0.2);
}

.history-item-content {
  flex: 1;
  cursor: pointer;
}

.history-item-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 5px 0;
  color: var(--text-color);
}

.history-item-desc {
  font-size: 13px;
  color: #888;
  margin: 0 0 8px 0;
}

.history-item-meta {
  display: flex;
  gap: 10px;
}

.history-item-actions {
  display: flex;
  gap: 8px;
}
</style>
