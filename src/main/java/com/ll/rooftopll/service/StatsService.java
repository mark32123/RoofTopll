package com.ll.rooftopll.service;

import com.ll.rooftopll.dto.ProgressChartDTO;

import java.util.Map;

public interface StatsService {
    // 获取用户三大项及总分的变化曲线数据
    ProgressChartDTO getProgressChart(Long userId);

    // 获取个人主页统计数据 (本周训练频率, 当前体重, 历史总重量)
    Map<String, Object> getHomeSummary(Long userId);
}
