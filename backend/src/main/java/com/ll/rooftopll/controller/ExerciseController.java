package com.ll.rooftopll.controller;

import com.ll.rooftopll.commn.api.Result;
import com.ll.rooftopll.entity.Exercise;
import com.ll.rooftopll.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    // 1. 获取可用动作列表
    @GetMapping("/list")
    public Result<List<Exercise>> list(@RequestParam(defaultValue = "0") Long userId) {
        return Result.success(exerciseService.getAvailableExercises(userId));
    }

    // 2. 模糊查询动作
    @GetMapping("/search")
    public Result<List<Exercise>> search(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") Long userId) {
        return Result.success(exerciseService.searchExercises(name, userId));
    }

    // 3. 获取三大项等常用动作
    @GetMapping("/common")
    public Result<List<Exercise>> getCommon() {
        return Result.success(exerciseService.getCommonExercises());
    }

    // 4. 用户新增自定义动作
    @PostMapping("/create")
    public Result<Void> create(@RequestBody Exercise exercise) {
        exerciseService.createCustomExercise(exercise);
        return Result.success();
    }
}