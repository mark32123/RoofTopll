package com.ll.rooftopll.controller;

import com.ll.rooftopll.commn.api.Result;
import com.ll.rooftopll.dto.WorkoutActivityDTO;
import com.ll.rooftopll.entity.WorkoutSession;
import com.ll.rooftopll.entity.WorkoutSet;
import com.ll.rooftopll.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @PostMapping("/save-set")
    public Result<Void> saveSet(@RequestParam Long userId,
                                @RequestParam Long exerciseId,
                                @RequestBody WorkoutSet workoutSet) {
        workoutService.saveWorkoutSet(userId, exerciseId, workoutSet);
        return Result.success();
    }

    @PostMapping("/start")
    public Result<WorkoutSession> startSession(@RequestParam Long userId, @RequestParam(required = false) String title) {
        // 调用 service 开启训练
        WorkoutSession session = workoutService.startNewSession(userId, title);
        return Result.success(session);
    }

    @PostMapping("/end")
    public Result<WorkoutSession> endSession(@RequestParam Long userId) {
        WorkoutSession summary = workoutService.endCurrentSession(userId);
        return Result.success(summary);
    }

}
