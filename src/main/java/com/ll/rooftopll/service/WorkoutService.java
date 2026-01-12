package com.ll.rooftopll.service;


import com.ll.rooftopll.dto.WorkoutActivityDTO;

import java.util.List;

public interface WorkoutService {
    // 开启训练课
    Long startSession(Long userId, String title);

    // 保存完整训练数据（包含多个动作及其对应的组数）
    void saveFullWorkout(Long userId, String title, List<WorkoutActivityDTO> activityList);

    // 结束训练
    void endSession(Long sessionId);
}
