package com.ll.rooftopll.service.impl;

import com.ll.rooftopll.dto.BigThreeDTO;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

        //若没有选择已有计划，则创建一个新计划
        if (currentSession == null) {
            currentSession = new WorkoutSession();
            currentSession.setUserId(userId);
            currentSession.setStartTime(LocalDateTime.now());
            currentSession.setTitle("新训练计划");
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
    // 开始训练开关
    @Transactional
    public WorkoutSession startNewSession(Long userId, String title) {
        // 1. 检查是否有正在进行的训练
        WorkoutSession activeSession = sessionMapper.findActiveSession(userId);
        if (activeSession != null) {
            // 如果有，可以直接返回，或者抛异常，这里我们选择直接返回当前的
            return activeSession;
        }

        // 2. 如果没有，创建新的
        WorkoutSession newSession = new WorkoutSession();
        newSession.setUserId(userId);
        newSession.setStartTime(LocalDateTime.now());

        // 如果没传标题，给个默认名
        if (title == null || title.isEmpty()) {
            title = LocalDate.now() + " 的力量训练";
        }
        newSession.setTitle(title);
        newSession.setTotalVolume(BigDecimal.ZERO);

        sessionMapper.insert(newSession);
        return newSession;
    }

    /*
    3. 结束训练
     */
    @Override
    @Transactional
    public WorkoutSession endCurrentSession(Long userId) {
        // 1. 找到当前活跃的 Session
        WorkoutSession activeSession = sessionMapper.findActiveSession(userId);
        if (activeSession == null) {
            throw new RuntimeException("没有正在进行的训练");
        }

        // 2. 计算本次训练的总容量 (Total Volume)
        // SQL: SELECT SUM(weight * reps) FROM workout_set WHERE activity_id IN (SELECT id FROM workout_activity WHERE session_id = ?)
        BigDecimal totalVolume = sessionMapper.calculateTotalVolume(activeSession.getId());

        // 3. 设置结束数据
        activeSession.setEndTime(LocalDateTime.now());
        activeSession.setTotalVolume(totalVolume != null ? totalVolume : BigDecimal.ZERO);

        // 4. 更新数据库
        sessionMapper.updateSessionEnd(activeSession);

        return activeSession;
    }

    @Override
    public List<BigThreeDTO> getBigThreeProgress(Long userId) {

        List<BigThreeDTO> rawData = sessionMapper.getBigThreeProgress(userId);

        BigDecimal lastSquat = BigDecimal.ZERO;
        BigDecimal lastBench = BigDecimal.ZERO;
        BigDecimal lastDeadlift = BigDecimal.ZERO;

        for (BigThreeDTO day : rawData) {
            if (day.getSquat().compareTo(BigDecimal.ZERO) == 0) {
                day.setSquat(lastSquat); // 如果今天没练，沿用上一次的
            } else {
                lastSquat = day.getSquat(); // 如果今天练了，更新“最近一次”记录
            }

            if (day.getBench().compareTo(BigDecimal.ZERO) == 0) {
                day.setBench(lastBench);
            } else {
                lastBench = day.getBench();
            }

            if (day.getDeadlift().compareTo(BigDecimal.ZERO) == 0) {
                day.setDeadlift(lastDeadlift);
            } else {
                lastDeadlift = day.getDeadlift();
            }

            day.setTotal(day.getSquat().add(day.getBench()).add(day.getDeadlift()));
        }

        return rawData;
    }
}