package com.ll.rooftopll.service.impl;
import com.ll.rooftopll.entity.User;
import com.ll.rooftopll.entity.WeightLog;
import com.ll.rooftopll.mapper.UserMapper;
import com.ll.rooftopll.mapper.WeightLogMapper;
import com.ll.rooftopll.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private WeightLogMapper weightLogMapper;
    @Autowired
    private UserMapper userMapper;

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

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user; // 登录成功
        }
        return null; // 登录失败
    }

    /**
     *  注册
     * @param user
     */
    public void save(User user) {
        // 检查用户名是否重复
        User existing = userMapper.findByUsername(user.getUsername());
        if (existing != null) {
            throw new RuntimeException("Mark，这个名字已经被占用了哦");
        }
        userMapper.insert(user);
    }
}