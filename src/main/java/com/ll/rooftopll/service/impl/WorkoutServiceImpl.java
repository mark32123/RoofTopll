package com.ll.rooftopll.service.impl;

import com.ll.rooftopll.dto.WorkoutActivityDTO;
import com.ll.rooftopll.entity.WorkoutActivity;
import com.ll.rooftopll.entity.WorkoutSession;
import com.ll.rooftopll.entity.WorkoutSet;
import com.ll.rooftopll.mapper.WorkoutActivityMapper;
import com.ll.rooftopll.mapper.WorkoutSessionMapper;
import com.ll.rooftopll.mapper.WorkoutSetMapper;
import com.ll.rooftopll.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class WorkoutServiceImpl implements WorkoutService {

    @Autowired private WorkoutSessionMapper sessionMapper;
    @Autowired private WorkoutActivityMapper activityMapper;
    @Autowired private WorkoutSetMapper setMapper;

    @Override
    public Long startSession(Long userId, String title) {
        WorkoutSession session = new WorkoutSession();
        session.setUserId(userId);
        session.setTitle(title);
        session.setStartTime(LocalDateTime.now());
        sessionMapper.insert(session);
        return session.getId();
    }

    @Override
    @Transactional // 保证事务一致性
    public void saveFullWorkout(Long userId, String title, List<WorkoutActivityDTO> activityList) {
        // 1. 创建并保存 Session
        WorkoutSession session = new WorkoutSession();
        session.setUserId(userId);
        session.setTitle(title);
        session.setStartTime(LocalDateTime.now());
        sessionMapper.insert(session);
        Long sessionId = session.getId();

        // 2. 遍历动作列表
        for (int i = 0; i < activityList.size(); i++) {
            WorkoutActivityDTO actDto = activityList.get(i);

            WorkoutActivity activity = new WorkoutActivity();
            activity.setSessionId(sessionId);
            activity.setExerciseId(actDto.getExerciseId());
            activity.setOrderNum(i + 1);
            activityMapper.insert(activity);

            // 3. 遍历并保存组数
            List<WorkoutSet> sets = actDto.getSets();
            if (sets != null) {
                for (int j = 0; j < sets.size(); j++) {
                    WorkoutSet set = sets.get(j);
                    set.setActivityId(activity.getId());
                    set.setSetIndex(j + 1);
                    setMapper.insert(set);
                }
            }
        }
    }

    @Override
    public void endSession(Long sessionId) {
        WorkoutSession session = sessionMapper.selectById(sessionId);
        if (session != null) {
            session.setEndTime(LocalDateTime.now());
            sessionMapper.updateById(session);
        }
    }
}
