-- 为 workout_plan 表添加 created_at 字段 (如果不存在)
ALTER TABLE workout_plan ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
