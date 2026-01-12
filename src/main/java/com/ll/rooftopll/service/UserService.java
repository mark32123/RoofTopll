package com.ll.rooftopll.service;


import com.ll.rooftopll.entity.User;

import java.math.BigDecimal;

public interface UserService {
    // 记录每日体重
    void logWeight(Long userId, BigDecimal weight);
    // 获取用户信息
    User getUserInfo(Long userId);
}
