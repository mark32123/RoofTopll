package com.ll.rooftopll.service.impl;

import com.ll.rooftopll.commn.fight.InspirationConsts;
import com.ll.rooftopll.dto.ActivityDetailDTO;
import com.ll.rooftopll.dto.BigThreeDTO;
import com.ll.rooftopll.dto.SessionSummaryDTO;
import com.ll.rooftopll.entity.*;
import com.ll.rooftopll.mapper.*;
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
    @Autowired
    private SupplementLogMapper supplementLogMapper;

    @Autowired
    private WorkoutPlanMapper planMapper;
    @Autowired
    private WorkoutPlanActivityMapper planActivityMapper;
    @Autowired
    private WorkoutPlanSetMapper planSetMapper;

    @Override
    @Transactional
    public void saveWorkoutSet(Long userId, Long exerciseId, WorkoutSet workoutSet) {
        WorkoutSession currentSession = sessionMapper.findActiveSession(userId);

        if (currentSession == null) {
            currentSession = new WorkoutSession();
            currentSession.setUserId(userId);
            currentSession.setStartTime(LocalDateTime.now());
            currentSession.setTitle("新训练计划");
            sessionMapper.insert(currentSession);
        }

        WorkoutActivity currentActivity = activityMapper.findActivity(currentSession.getId(), exerciseId);

        if (currentActivity == null) {
            currentActivity = new WorkoutActivity();
            currentActivity.setSessionId(currentSession.getId());
            currentActivity.setExerciseId(exerciseId);
            int actCount = activityMapper.countBySessionId(currentSession.getId());
            currentActivity.setOrderNum(actCount + 1);
            activityMapper.insert(currentActivity);
        }

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
    public SessionSummaryDTO endCurrentSession(Long userId) {

        WorkoutSession session = sessionMapper.findActiveSession(userId);
        if (session == null) throw new RuntimeException("Mark，当前没有正在进行的训练哦");

        // 结束训练并计算各项指标
        LocalDateTime now = LocalDateTime.now();
        session.setEndTime(now);

        long mins = java.time.Duration.between(session.getStartTime(), now).toMinutes();
        BigDecimal totalVol = sessionMapper.calculateTotalVolume(session.getId());
        session.setTotalVolume(totalVol != null ? totalVol : BigDecimal.ZERO);

        sessionMapper.updateSessionEnd(session);

        // 训练详情
        List<ActivityDetailDTO> details = sessionMapper.getSessionDetails(session.getId());

        SessionSummaryDTO summary = new SessionSummaryDTO();

        // 补剂记录
        List<SupplementLog> supplements = supplementLogMapper.selectBySessionId(session.getId());
        summary.setSupplements(supplements);

        // 赋予进步标签
        for (ActivityDetailDTO activity : details) {
            activity.setAchievementTag(InspirationConsts.TAG_NORMAL);
        }

        summary.setSessionId(session.getId());
        summary.setStartTime(session.getStartTime());
        summary.setEndTime(now);
        summary.setDurationMinutes(mins);
        summary.setTotalVolume(session.getTotalVolume());
        summary.setActivities(details);
        summary.setSessionInspiration(InspirationConsts.getSessionInspiration(mins, session.getTotalVolume()));

        return summary;
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

    @Override
    public SessionSummaryDTO getSessionSummary(Long sessionId) {
        // 1. 获取 Session 基础信息（时间、总容量等）
        WorkoutSession session = sessionMapper.selectById(sessionId);

        // 2. 获取该 Session 下所有的动作及其对应的组数
        // 建议在 Mapper 层面通过 JOIN 一次性查出，或者分步查询
        List<ActivityDetailDTO> activities = sessionMapper.getSessionDetails(sessionId);

        SessionSummaryDTO summary = new SessionSummaryDTO();
        summary.setSessionId(session.getId());
        summary.setStartTime(session.getStartTime());
        summary.setEndTime(session.getEndTime());
        summary.setTotalVolume(session.getTotalVolume());
        summary.setActivities(activities);

        return summary;
    }

    // WorkoutServiceImpl.java
    @Override
    public void logSupplement(SupplementLog log) {
        // 1. 健壮性检查：如果没有手动设置时间，自动填充当前时间
        if (log.getTakenTime() == null) {
            log.setTakenTime(LocalDateTime.now());
        }

        // 2. 调用 Mapper 插入数据库
        // 注意：这里需要注入你新创建的 SupplementLogMapper
        int rows = supplementLogMapper.insert(log);

        if (rows <= 0) {
            throw new RuntimeException("Mark，补剂数据存入失败了，检查下数据库连接？");
        }
    }

    @Override
    public List<WorkoutPlan> getAllWorkoutPlans(Long userId) {
        return planMapper.findByUserId(userId);
    }

    @Override
    public WorkoutPlan getWorkoutPlanById(Long planId) {
        return planMapper.findById(planId);
    }

    @Override
    @Transactional
    public WorkoutPlan createWorkoutPlan(WorkoutPlan workoutPlan) {
        planMapper.insert(workoutPlan);
        
        // 空值检查：如果没有活动，直接返回
        if (workoutPlan.getActivities() == null || workoutPlan.getActivities().isEmpty()) {
            return workoutPlan;
        }
        
        for (WorkoutPlanActivity activity : workoutPlan.getActivities()) {
            activity.setPlanId(workoutPlan.getId());
            planActivityMapper.insert(activity);
            
            // 空值检查：如果该活动没有组数，跳过
            if (activity.getSets() != null) {
                for (WorkoutPlanSet set : activity.getSets()) {
                    set.setPlanActivityId(activity.getId());
                    planSetMapper.insert(set);
                }
            }
        }
        return workoutPlan;
    }

    @Override
    @Transactional
    public WorkoutPlan updateWorkoutPlan(Long planId, WorkoutPlan workoutPlan) {
        workoutPlan.setId(planId);
        planMapper.update(workoutPlan);

        // 删除旧的 activities 和 sets
        List<WorkoutPlanActivity> oldActivities = planActivityMapper.findByPlanId(planId);
        if (!oldActivities.isEmpty()) {
            List<Long> oldActivityIds = oldActivities.stream().map(WorkoutPlanActivity::getId).toList();
            planSetMapper.deleteByActivityIds(oldActivityIds);
            planActivityMapper.deleteByPlanId(planId);
        }

        // 空值检查：如果没有活动，直接返回
        if (workoutPlan.getActivities() == null || workoutPlan.getActivities().isEmpty()) {
            return workoutPlan;
        }

        // 插入新的 activities 和 sets
        for (WorkoutPlanActivity activity : workoutPlan.getActivities()) {
            activity.setPlanId(planId);
            planActivityMapper.insert(activity);
            
            // 空值检查：如果该活动没有组数，跳过
            if (activity.getSets() != null) {
                for (WorkoutPlanSet set : activity.getSets()) {
                    set.setPlanActivityId(activity.getId());
                    planSetMapper.insert(set);
                }
            }
        }
        return workoutPlan;
    }

    @Override
    @Transactional
    public void deleteWorkoutPlan(Long planId) {
        List<WorkoutPlanActivity> activities = planActivityMapper.findByPlanId(planId);
        if (!activities.isEmpty()) {
            List<Long> activityIds = activities.stream().map(WorkoutPlanActivity::getId).toList();
            planSetMapper.deleteByActivityIds(activityIds);
            planActivityMapper.deleteByPlanId(planId);
        }
        planMapper.delete(planId);
    }
}