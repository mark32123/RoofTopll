package com.ll.rooftopll.service;



import com.ll.rooftopll.entity.WeightLog;


import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    // 记录或更新当日体重
    void logWeight(Long userId, BigDecimal weight);

    // 获取体重历史数据（用于画曲线图）
    List<WeightLog> getWeightHistory(Long userId);
}
