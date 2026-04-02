package com.ll.rooftopll.controller;

import com.ll.rooftopll.commn.api.Result;
import com.ll.rooftopll.entity.User;
import com.ll.rooftopll.entity.WeightLog;
import com.ll.rooftopll.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
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

    // 登录
    @PostMapping("/login")
    public Result<User> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userService.login(username, password);
        if (user != null) {
            // 登录成功，把用户信息存在 Session 里
            session.setAttribute("currentUser", user);
            return Result.success(user);
        }
        return Result.error("用户名或密码错误！");
    }

    // 注册
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        userService.save(user);
        return Result.success("欢迎加入，RoofTopII！账号已就绪.");
    }
}
