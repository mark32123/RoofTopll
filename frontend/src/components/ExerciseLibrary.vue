<template>
  <div class="exercise-library">
    <!-- 搜索栏和操作栏 -->
    <div class="header-bar">
      <div class="search-bar">
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="搜索动作名称..."
          class="search-input"
        >
        <button v-if="searchQuery" class="btn-clear" @click="searchQuery = ''">×</button>
      </div>
      <button class="btn-add-exercise" @click="showAddModal = true" title="添加动作">
        <span>+</span>
      </button>
    </div>

    <div class="exercise-container">
      <!-- 左侧分类导航 -->
      <div class="category-sidebar">
        <div 
          v-for="category in filteredCategories" 
          :key="category.name"
          :class="['category-item', { active: activeCategory === category.name }]"
          @click="scrollToCategory(category.name)"
        >
          {{ category.name }}
          <span class="category-count">{{ category.count }}</span>
        </div>
      </div>

      <!-- 右侧动作列表 -->
      <div class="exercises-content" ref="contentRef" @scroll="handleScroll">
        <div 
          v-for="category in filteredCategories" 
          :key="category.name"
          :id="'category-' + category.name"
          class="category-section"
        >
          <h3 class="category-title">{{ category.name }}</h3>
          <div class="exercise-grid">
            <div 
              v-for="exercise in category.exercises" 
              :key="exercise.id"
              class="exercise-card"
              @click="selectExercise(exercise)"
            >
              <button 
                type="button" 
                class="btn-delete-exercise" 
                @click.stop="deleteExercise(exercise)"
                title="删除动作"
              >
                <span>−</span>
              </button>
              <div class="exercise-info">
                <h4 class="exercise-name">{{ exercise.name }}</h4>
                <div class="exercise-tags">
                  <span v-if="exercise.isBigThree" class="tag big-three">三大项</span>
                  <span v-if="exercise.userId !== 0" class="tag custom">自定义</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 空状态 -->
        <div v-if="filteredCategories.length === 0" class="empty-state">
          <p>没有找到相关动作</p>
        </div>
      </div>
    </div>

    <!-- 动作详情弹窗 -->
    <Modal :show="showExerciseDetail" @close="showExerciseDetail = false">
      <div class="exercise-detail-modal">
        <div class="modal-header">
          <h3>{{ selectedExercise?.name }}</h3>
        </div>
        
        <div class="exercise-detail-content">
          <div class="detail-row">
            <span class="detail-label">部位分类:</span>
            <span class="detail-value">{{ selectedExercise?.category }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">类型:</span>
            <span class="detail-value">
              <span v-if="selectedExercise?.isBigThree" class="tag big-three">三大项</span>
              <span v-else class="tag">普通动作</span>
            </span>
          </div>
          <div class="detail-row">
            <span class="detail-label">来源:</span>
            <span class="detail-value">
              {{ selectedExercise?.userId === 0 ? '系统预设' : '用户自定义' }}
            </span>
          </div>
        </div>

        <div class="modal-actions">
          <button class="btn btn-secondary" @click="showExerciseDetail = false">关闭</button>
          <button class="btn btn-primary" @click="useExercise(selectedExercise)">使用此动作</button>
        </div>
      </div>
    </Modal>

    <!-- 添加动作弹窗 -->
    <Modal :show="showAddModal" @close="showAddModal = false">
      <div class="add-exercise-modal">
        <div class="modal-header">
          <h3>添加新动作</h3>
          <button class="btn-icon-close" @click="showAddModal = false">×</button>
        </div>
        
        <form @submit.prevent="submitNewExercise">
          <div class="form-group">
            <label for="exercise-name">动作名称 *</label>
            <input 
              id="exercise-name" 
              v-model="newExercise.name" 
              type="text" 
              required 
              placeholder="例如：哑铃卧推"
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label>部位分类 *</label>
            <div class="category-grid">
              <button 
                type="button"
                v-for="cat in categoryOptions" 
                :key="cat" 
                :class="['category-option', { active: newExercise.category === cat }]"
                @click="newExercise.category = cat"
              >
                {{ cat }}
              </button>
            </div>
            <div v-if="!newExercise.category" class="field-error">请选择部位分类</div>
          </div>
          
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" v-model="newExercise.isBigThree">
              <span>设为三大项</span>
            </label>
          </div>
          
          <div class="form-actions">
            <button type="button" class="btn btn-secondary" @click="showAddModal = false">取消</button>
            <button type="submit" class="btn btn-primary">添加动作</button>
          </div>
        </form>
      </div>
    </Modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import Modal from './Modal.vue'

const exercises = ref<any[]>([])
const activeCategory = ref('')
const showExerciseDetail = ref(false)
const showAddModal = ref(false)
const selectedExercise = ref<any>(null)
const contentRef = ref<HTMLElement | null>(null)
const searchQuery = ref('')

// 部位选项
const categoryOptions = [
  '胸', '背', '腿', '肩', '臀', '手臂', '核心', '竖脊肌',
  '腿/全身', '背/全身', '胸/全身'
]

// 新动作表单
const newExercise = ref({
  name: '',
  category: '',
  isBigThree: false
})

// 按部位分类 (支持搜索过滤)
const filteredCategories = computed(() => {
  let result = exercises.value;
  
  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(ex => ex.name.toLowerCase().includes(query));
  }
  
  const categoryMap: Record<string, any[]> = {};
  
  result.forEach(exercise => {
    if (!categoryMap[exercise.category]) {
      categoryMap[exercise.category] = [];
    }
    categoryMap[exercise.category].push(exercise);
  });

  return Object.entries(categoryMap).map(([name, exercises]) => ({
    name,
    count: exercises.length,
    exercises
  }));
});

async function fetchExercises() {
  try {
    // 传递当前用户 ID=1,这样会返回系统预设 (user_id=0) 和用户自定义 (user_id=1) 的动作
    const response = await fetch('/api/exercises/list?userId=1');
    console.log('响应状态:', response.status);
    
    if (response.ok) {
      const data = await response.json();
      console.log('获取到的数据:', data);
      exercises.value = data.data || [];
      console.log('动作数量:', exercises.value.length);
      
      // 默认选中第一个分类
      if (filteredCategories.value.length > 0) {
        activeCategory.value = filteredCategories.value[0].name;
      }
    } else {
      console.error('请求失败，状态码:', response.status);
    }
  } catch (error) {
    console.error('获取动作列表失败:', error);
  }
}

function scrollToCategory(categoryName: string) {
  activeCategory.value = categoryName;
  const element = document.getElementById('category-' + categoryName);
  if (element && contentRef.value) {
    contentRef.value.scrollTo({
      top: element.offsetTop - contentRef.value.offsetTop,
      behavior: 'smooth'
    });
  }
}

function handleScroll() {
  if (!contentRef.value) return;
  
  const scrollTop = contentRef.value.scrollTop;
  const tolerance = 100; // 容差值
  
  // 找到当前可见的第一个分类
  for (const category of filteredCategories.value) {
    const element = document.getElementById('category-' + category.name);
    if (element) {
      const elementTop = element.offsetTop - contentRef.value.offsetTop;
      const elementBottom = elementTop + element.offsetHeight;
      
      if (scrollTop + tolerance >= elementTop && scrollTop < elementBottom) {
        if (activeCategory.value !== category.name) {
          activeCategory.value = category.name;
        }
        break;
      }
    }
  }
}

function selectExercise(exercise: any) {
  selectedExercise.value = exercise;
  showExerciseDetail.value = true;
}

function useExercise(exercise: any) {
  // TODO: 实现使用动作的逻辑 (添加到训练计划等)
  console.log('使用动作:', exercise);
  alert('已选择：' + exercise.name);
  showExerciseDetail.value = false;
}

async function submitNewExercise() {
  // 验证部位是否选择
  if (!newExercise.value.category) {
    alert('请选择部位分类!');
    return;
  }
  
  try {
    const response = await fetch('/api/exercises/create', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        ...newExercise.value,
        userId: 1 // 用户自定义动作
      })
    });

    if (response.ok) {
      alert('动作添加成功!');
      showAddModal.value = false;
      // 清空表单
      newExercise.value = {
        name: '',
        category: '',
        isBigThree: false
      };
      // 重新加载动作列表
      await fetchExercises();
    } else {
      console.error('添加动作失败');
      alert('添加失败，请重试');
    }
  } catch (error) {
    console.error('请求后端 API 时出错:', error);
    alert('添加失败，请重试');
  }
}

async function deleteExercise(exercise: any) {
  if (!confirm(`确定要删除动作 "${exercise.name}" 吗？`)) {
    return;
  }
  
  console.log('准备删除动作 ID:', exercise.id);
  console.log('请求 URL:', `/api/exercises/${exercise.id}`);
  
  try {
    const response = await fetch(`/api/exercises/${exercise.id}`, {
      method: 'DELETE'
    });

    console.log('删除响应状态:', response.status);

    if (response.ok) {
      alert('动作已删除!');
      // 重新加载动作列表
      await fetchExercises();
    } else {
      const errorText = await response.text();
      console.error('删除失败，响应内容:', errorText);
      alert('删除失败，请重试');
    }
  } catch (error) {
    console.error('请求后端 API 时出错:', error);
    alert('删除失败，请重试');
  }
}

onMounted(() => {
  fetchExercises();
});
</script>

<style scoped>
.exercise-library {
  padding: 0;
  height: 100vh;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 搜索栏和操作栏 */
.header-bar {
  padding: 15px 20px;
  display: flex;
  gap: 15px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.search-bar {
  flex: 1;
  position: relative;
}

.search-input {
  width: 100%;
  padding: 12px 40px 12px 15px;
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-color);
  font-size: 14px;
}

.search-input:focus {
  outline: none;
  border-color: rgba(255, 71, 87, 0.5);
}

.btn-clear {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #aaa;
  font-size: 18px;
  transition: all 0.2s ease;
}

.btn-clear:hover {
  background: rgba(255, 71, 87, 0.2);
  color: #ff6b7a;
}

/* 添加动作按钮 */
.btn-add-exercise {
  flex-shrink: 0;
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: linear-gradient(135deg, #ff4757, #ff6b7a);
  border: none;
  color: white;
  font-size: 28px;
  line-height: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(255, 71, 87, 0.3);
}

.btn-add-exercise:hover {
  background: linear-gradient(135deg, #ff6b7a, #ff8fa3);
  box-shadow: 0 6px 20px rgba(255, 71, 87, 0.4);
  transform: translateY(-2px) scale(1.05);
}

.btn-add-exercise:active {
  transform: translateY(0) scale(0.98);
  box-shadow: 0 2px 8px rgba(255, 71, 87, 0.3);
}

.exercise-container {
  display: flex;
  gap: 0;
  flex: 1;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 0;
  overflow: hidden;
}

/* 左侧分类导航 */
.category-sidebar {
  flex: 0 0 110px;
  min-width: 90px;
  max-width: 130px;
  background: rgba(0, 0, 0, 0.3);
  overflow-y: auto;
  border-right: 1px solid rgba(255, 255, 255, 0.1);
}

.category-item {
  padding: 22px 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-left: 3px solid transparent;
  color: #aaa;
  font-size: 15px;
  gap: 8px;
}

.category-item:hover {
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-color);
}

.category-item.active {
  background: rgba(255, 71, 87, 0.1);
  border-left-color: #ff4757;
  color: #ff6b7a;
  font-weight: 600;
}

.category-count {
  background: rgba(100, 100, 100, 0.3);
  color: #888;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 12px;
  white-space: nowrap;
}

.category-item.active .category-count {
  background: rgba(255, 71, 87, 0.2);
  color: #ff6b7a;
}

/* 右侧内容区域 */
.exercises-content {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
  height: 100%;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #888;
}

.category-section {
  margin-bottom: 30px;
}

.category-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 15px;
  color: var(--text-color);
  padding-bottom: 10px;
  border-bottom: 2px solid rgba(255, 71, 87, 0.3);
}

.exercise-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 15px;
}

.exercise-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 10px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
}

.exercise-card:hover {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 71, 87, 0.3);
  transform: translateY(-2px);
}

/* 删除动作按钮 */
.btn-delete-exercise {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: rgba(255, 71, 87, 0.15);
  border: 1px solid rgba(255, 71, 87, 0.3);
  color: #ff6b7a;
  font-size: 24px;
  line-height: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  z-index: 10;
  padding: 0;
}

.btn-delete-exercise:hover {
  background: rgba(255, 71, 87, 0.25);
  border-color: rgba(255, 71, 87, 0.5);
  transform: scale(1.1);
  box-shadow: 0 2px 8px rgba(255, 71, 87, 0.3);
}

.btn-delete-exercise:active {
  transform: scale(0.95);
}

.exercise-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.exercise-name {
  font-size: 15px;
  font-weight: 500;
  margin: 0;
  color: var(--text-color);
}

.exercise-tags {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.tag {
  background: rgba(100, 100, 100, 0.2);
  color: #aaa;
  padding: 2px 8px;
  border-radius: 6px;
  font-size: 11px;
}

.tag.big-three {
  background: rgba(255, 71, 87, 0.2);
  color: #ff6b7a;
}

.tag.custom {
  background: rgba(76, 175, 80, 0.2);
  color: #81c784;
}

/* 删除按钮 */
.btn-delete {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: rgba(255, 71, 87, 0.2);
  border: 1px solid rgba(255, 71, 87, 0.4);
  color: #ff4757;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  opacity: 1;
  padding: 0;
}

.btn-delete:hover {
  background: rgba(255, 71, 87, 0.4);
  border-color: rgba(255, 71, 87, 0.7);
  color: #fff;
  transform: scale(1.05);
}

.btn-delete:active {
  transform: scale(0.95);
}

/* 动作详情弹窗 */
.exercise-detail-modal {
  max-width: 500px;
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

.exercise-detail-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.detail-row {
  display: flex;
  gap: 10px;
  align-items: center;
}

.detail-label {
  min-width: 80px;
  color: #888;
  font-size: 14px;
}

.detail-value {
  color: var(--text-color);
  font-size: 14px;
  flex: 1;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 添加动作弹窗 */
.add-exercise-modal {
  max-width: 500px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-color);
}

.form-group input[type="text"],
.form-group select {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-color);
  font-size: 14px;
  transition: all 0.2s ease;
}

.form-input {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-color);
  font-size: 14px;
  transition: all 0.2s ease;
}

.form-input:focus {
  outline: none;
  border-color: rgba(255, 71, 87, 0.5);
  background: rgba(255, 255, 255, 0.08);
}

.form-group input[type="text"]:focus,
.form-group select:focus {
  outline: none;
  border-color: rgba(255, 71, 87, 0.5);
  background: rgba(255, 255, 255, 0.08);
}

.field-error {
  color: #ff6b7a;
  font-size: 12px;
  margin-top: 5px;
}

.form-group input[type="text"],
.form-group select {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-color);
  font-size: 14px;
  transition: all 0.2s ease;
}

.form-group input[type="text"]:focus,
.form-group select:focus {
  outline: none;
  border-color: rgba(255, 71, 87, 0.5);
  background: rgba(255, 255, 255, 0.08);
}

/* 部位选择网格 */
.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 8px;
}

.category-option {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  color: var(--text-color);
  font-size: 14px;
  text-align: center;
}

.category-option:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.3);
}

.category-option.active {
  background: rgba(255, 71, 87, 0.2);
  border-color: rgba(255, 71, 87, 0.5);
  color: #ff6b7a;
  font-weight: 600;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  color: var(--text-color);
  font-size: 14px;
}

.checkbox-label input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 25px;
}
</style>
