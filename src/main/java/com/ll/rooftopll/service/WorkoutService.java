package com.ll.rooftopll.service;


import com.ll.rooftopll.dto.BigThreeDTO;
import com.ll.rooftopll.dto.SessionSummaryDTO;
import com.ll.rooftopll.entity.WorkoutSession;
import com.ll.rooftopll.entity.WorkoutSet;

import java.util.List;

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
    SessionSummaryDTO endCurrentSession(Long userId);

    /**
     * 获取三大项成绩
     * @param userId
     * @return
     */
    List<BigThreeDTO> getBigThreeProgress(Long userId);

    /**
     * 获取训练报告
     * @param sessionId
     * @return
     */
    SessionSummaryDTO getSessionSummary(Long sessionId);
}
