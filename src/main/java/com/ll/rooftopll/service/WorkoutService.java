package com.ll.rooftopll.service;


import com.ll.rooftopll.entity.WorkoutSession;
import com.ll.rooftopll.entity.WorkoutSet;

public interface WorkoutService {
    /**
     * 保存单组训练数据
     * 逻辑：自动关联或创建 Session 和 Activity
     */
    void saveWorkoutSet(Long userId, Long exerciseId, WorkoutSet workoutSet);

    /**
     * 开始训练计划
     * 逻辑：自动关联或创建 Session 和 Activity
     */
    WorkoutSession startNewSession(Long userId, String title);

    /**
     * 结束当前训练计划
     * 逻辑：自动关联或创建 Session 和 Activity
     */
    WorkoutSession endCurrentSession(Long userId);
}
