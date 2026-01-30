package com.ll.rooftopll.controller;

import com.ll.rooftopll.commn.api.Result;
import com.ll.rooftopll.entity.User;
import com.ll.rooftopll.entity.WeightLog;
import com.ll.rooftopll.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    // 记录体重
    @PostMapping("/weight")
    public Result<String> logWeight(@RequestParam Long userId, @RequestParam BigDecimal weight) {
        userService.logWeight(userId, weight);
        return Result.success("体重记录成功");
    }

    // 获取体重变化数据
    @GetMapping("/weight/history")
    public Result<List<WeightLog>> getWeightHistory(@RequestParam Long userId) {
        List<WeightLog> history = userService.getWeightHistory(userId);
        return Result.success(history);
    }

}
