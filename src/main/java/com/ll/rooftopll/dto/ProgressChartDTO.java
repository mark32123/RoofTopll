package com.ll.rooftopll.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProgressChartDTO {
    private List<String> dates;          // 横坐标：日期列表
    private List<Double> squat1RMs;     // 深蹲曲线数据
    private List<Double> bench1RMs;     // 卧推曲线数据
    private List<Double> deadlift1RMs;  // 硬拉曲线数据
    private List<Double> totalScores;    // 总成绩曲线数据
}
