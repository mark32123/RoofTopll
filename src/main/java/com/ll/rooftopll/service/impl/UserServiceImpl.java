package com.ll.rooftopll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.rooftopll.entity.User;
import com.ll.rooftopll.entity.WeightLog;
import com.ll.rooftopll.mapper.UserMapper;
import com.ll.rooftopll.mapper.WeightLogMapper;
import com.ll.rooftopll.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeightLogMapper weightLogMapper;

    @Override
    public void logWeight(Long userId, BigDecimal weight) {
        LocalDate today = LocalDate.now();
        // 检查今天是否已经记录过体重
        WeightLog existingLog = weightLogMapper.selectOne(
                new QueryWrapper<WeightLog>()
                        .eq("user_id", userId)
                        .eq("log_date", today)
        );

        if (existingLog != null) {
            // 如果已存在则更新
            existingLog.setWeight(weight);
            weightLogMapper.updateById(existingLog);
        } else {
            // 不存在则新增
            WeightLog newLog = new WeightLog();
            newLog.setUserId(userId);
            newLog.setWeight(weight);
            newLog.setLogDate(today);
            weightLogMapper.insert(newLog);
        }
    }

    @Override
    public List<WeightLog> getWeightHistory(Long userId) {
        return weightLogMapper.selectList(
                new QueryWrapper<WeightLog>()
                        .eq("user_id", userId)
                        .orderByAsc("log_date")
        );
    }

    @Override
    public void register(User user) {
        // 实际开发这里需要对密码进行加密（如 BCrypt）
        userMapper.insert(user);
    }

    @Override
    public User getUserInfo(Long userId) {
        return userMapper.selectById(userId);
    }
}
