package com.ll.rooftopll.service.impl;
import com.ll.rooftopll.entity.WeightLog;
import com.ll.rooftopll.mapper.WeightLogMapper;
import com.ll.rooftopll.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private WeightLogMapper weightLogMapper;

    /**
     * 记录或更新当日体重
     * @param userId
     * @param weight
     */
    public void logWeight(Long userId, BigDecimal weight) {
        WeightLog log = new WeightLog();
        log.setUserId(userId);
        log.setWeight(weight);
        log.setLogDate(LocalDate.now()); // 设置为当前日期

        weightLogMapper.upsertWeight(log);
    }

    /**
     * 获取体重历史数据
     * @param userId
     * @return
     */
    public List<WeightLog> getWeightHistory(Long userId) {
        return weightLogMapper.selectWeightHistory(userId);
    }
}