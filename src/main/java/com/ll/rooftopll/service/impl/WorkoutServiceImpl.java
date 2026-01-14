package com.ll.rooftopll.service.impl;

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

@Service
public class WorkoutServiceImpl implements WorkoutService {


    @Autowired
    private WorkoutSessionMapper sessionMapper;
    @Autowired
    private WorkoutActivityMapper activityMapper;
    @Autowired
    private WorkoutSetMapper setMapper;

    @Override
    @Transactional // 👈 必须开启事务，防止数据入库一半报错
    public void saveWorkoutSet(Long userId, Long exerciseId, WorkoutSet workoutSet) {
        // 1. 获取或创建 Session
        WorkoutSession currentSession = sessionMapper.findActiveSession(userId);

        if (currentSession == null) {
            currentSession = new WorkoutSession();
            currentSession.setUserId(userId);
            currentSession.setStartTime(LocalDateTime.now());
            currentSession.setTitle("新训练");
            sessionMapper.insert(currentSession); // 插入后，id 会自动回填
        }

        //2. 获取或创建 Activity (动作)
        WorkoutActivity currentActivity = activityMapper.findActivity(currentSession.getId(), exerciseId);

        if (currentActivity == null) {
            currentActivity = new WorkoutActivity();
            currentActivity.setSessionId(currentSession.getId());
            currentActivity.setExerciseId(exerciseId);
            // 自动计算这是今天第几个动作
            int actCount = activityMapper.countBySessionId(currentSession.getId());
            currentActivity.setOrderNum(actCount + 1);
            activityMapper.insert(currentActivity);
        }

        // 3. 保存 Set (组数)
        workoutSet.setActivityId(currentActivity.getId());
        int setCount = setMapper.countByActivityId(currentActivity.getId());
        workoutSet.setSetIndex(setCount + 1);

        setMapper.insert(workoutSet);

    }
}