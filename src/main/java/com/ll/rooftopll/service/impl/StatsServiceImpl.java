package com.ll.rooftopll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.rooftopll.commn.utils.FormulaUtils;
import com.ll.rooftopll.dto.ProgressChartDTO;
import com.ll.rooftopll.entity.Exercise;
import com.ll.rooftopll.entity.WeightLog;
import com.ll.rooftopll.entity.WorkoutSession;
import com.ll.rooftopll.mapper.ExerciseMapper;
import com.ll.rooftopll.mapper.WeightLogMapper;
import com.ll.rooftopll.mapper.WorkoutSessionMapper;
import com.ll.rooftopll.mapper.WorkoutSetMapper;
import com.ll.rooftopll.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired private WorkoutSetMapper setMapper;
    @Autowired private ExerciseMapper exerciseMapper;
    @Autowired private WorkoutSessionMapper sessionMapper;
    @Autowired private WeightLogMapper weightLogMapper;
    @Override
    public ProgressChartDTO getProgressChart(Long userId) {
        // 1. 获取包含日期、动作名称和成绩的原始数据（调用我们自定义的 SQL）
        List<Map<String, Object>> rawData = setMapper.selectBigThreeHistory(userId);

        // 2. 将数据按日期归档，找出每一天每个动作的最高 1RM
        TreeMap<String, Map<String, Double>> dateGroupedData = new TreeMap<>();
        for (Map<String, Object> row : rawData) {
            String date = row.get("training_date").toString();
            String exerciseName = row.get("exercise_name").toString();
            BigDecimal weight = (BigDecimal) row.get("weight");
            Integer reps = (Integer) row.get("reps");

            // 使用工具类计算 1RM
            double oneRM = FormulaUtils.calculateOneRM(weight, reps).doubleValue();

            dateGroupedData.putIfAbsent(date, new HashMap<>());
            Map<String, Double> dayMap = dateGroupedData.get(date);
            if (oneRM > dayMap.getOrDefault(exerciseName, 0.0)) {
                dayMap.put(exerciseName, oneRM);
            }
        }

        // 3. 准备返回结果，应用“数据沿用”逻辑
        ProgressChartDTO dto = new ProgressChartDTO();
        List<String> dates = new ArrayList<>(dateGroupedData.keySet());
        List<Double> squatList = new ArrayList<>();
        List<Double> benchList = new ArrayList<>();
        List<Double> deadliftList = new ArrayList<>();
        List<Double> totalList = new ArrayList<>();

        // 初始化“上一次”成绩记录
        double lastS = 0, lastB = 0, lastD = 0;

        for (String date : dates) {
            Map<String, Double> dayMap = dateGroupedData.get(date);

            // 核心逻辑：如果当天没练某项，则取 lastValue (上一次的成绩)
            double currentS = findBigThreeValue(dayMap, "深蹲", lastS);
            double currentB = findBigThreeValue(dayMap, "卧推", lastB);
            double currentD = findBigThreeValue(dayMap, "硬拉", lastD);

            squatList.add(currentS);
            benchList.add(currentB);
            deadliftList.add(currentD);
            totalList.add(currentS + currentB + currentD); // 计算当日总成绩

            // 更新历史记录值，供下一个循环（下一天）使用
            lastS = currentS;
            lastB = currentB;
            lastD = currentD;
        }

        dto.setDates(dates);
        dto.setSquat1RMs(squatList);
        dto.setBench1RMs(benchList);
        dto.setDeadlift1RMs(deadliftList);
        dto.setTotalScores(totalList);
        return dto;
    }

    // 模糊匹配动作名称的方法
    private double findBigThreeValue(Map<String, Double> dayMap, String key, double lastValue) {
        for (String exerciseName : dayMap.keySet()) {
            if (exerciseName.contains(key)) {
                return dayMap.get(exerciseName);
            }
        }
        return lastValue;
    }

    @Override
    public Map<String, Object> getHomeSummary(Long userId) {
        Map<String, Object> summary = new HashMap<>();

        // 1. 查询总训练次数 (Session数量)
        Long workoutCount = sessionMapper.selectCount(
                new QueryWrapper<WorkoutSession>().eq("user_id", userId)
        );

        // 2. 查询最近一次体重记录 (从 weight_log 表按日期倒序取第一条)
        WeightLog latestWeight = weightLogMapper.selectOne(
                new QueryWrapper<WeightLog>()
                        .eq("user_id", userId)
                        .orderByDesc("log_date")
                        .last("LIMIT 1")
        );

        // 3. 查询历史总容量 (所有 Set 的 weight * reps 之和)
        // 建议在 WorkoutSetMapper 编写自定义 SQL，此处先放逻辑占位
        Double totalVolume = setMapper.sumTotalVolumeByUserId(userId);

        summary.put("workoutCount", workoutCount != null ? workoutCount : 0);
        summary.put("currentWeight", latestWeight != null ? latestWeight.getWeight() : 0);
        summary.put("totalVolume", totalVolume != null ? totalVolume : 0);

        return summary;
    }
}
