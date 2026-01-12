package com.ll.rooftopll.service;


import com.ll.rooftopll.entity.User;
import com.ll.rooftopll.entity.WeightLog;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    // 记录/更新今日体重
    void logWeight(Long userId, BigDecimal weight);

    // 获取用户体重历史记录（用于绘制个人体重曲线）
    List<WeightLog> getWeightHistory(Long userId);

    // 用户注册
    void register(User user);

    // 获取用户信息
    User getUserInfo(Long userId);
}
