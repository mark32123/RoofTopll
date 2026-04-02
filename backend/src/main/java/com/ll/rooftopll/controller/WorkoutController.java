package com.ll.rooftopll.controller;

import com.ll.rooftopll.commn.api.Result;
import com.ll.rooftopll.dto.BigThreeDTO;
import com.ll.rooftopll.dto.SessionSummaryDTO;
import com.ll.rooftopll.entity.User;
import com.ll.rooftopll.entity.WorkoutPlan;
import com.ll.rooftopll.entity.WorkoutSession;
import com.ll.rooftopll.entity.WorkoutSet;
import com.ll.rooftopll.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/workout")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    /**
     * 设置计划：重量，组数，次数
     * @param userId
     * @param exerciseId
     * @param workoutSet
     * @return
     */
    @PostMapping("/save-set")
    public Result<Void> saveSet(@RequestParam Long userId,
                                @RequestParam Long exerciseId,
                                @RequestBody WorkoutSet workoutSet) {
        workoutService.saveWorkoutSet(userId, exerciseId, workoutSet);
        return Result.success();
    }

    /**
     * 开启训练
     * @param userId
     * @param title
     * @return
     */
    @PostMapping("/start")
    public Result<WorkoutSession> startSession(@RequestParam Long userId, @RequestParam(required = false) String title) {
        // 调用 service 开启训练
        WorkoutSession session = workoutService.startNewSession(userId, title);
        return Result.success(session);
    }

    /**
     * 结束训练
     * @param userId
     * @return
     */
    @PostMapping("/end")
    public Result<SessionSummaryDTO> endSession(@RequestParam Long userId) {
        SessionSummaryDTO summary = workoutService.endCurrentSession(userId);
        return Result.success(summary);
    }

    /**
     * 查询三大项及各项成绩用于曲线图绘制
     * @param userId
     * @return
     */
    @GetMapping("/stats/big-three")
    public Result<List<BigThreeDTO>> getBigThreeStats(@RequestParam Long userId) {
        return Result.success(workoutService.getBigThreeProgress(userId));
    }

    @PostMapping("/plan")
    public Result<WorkoutPlan> createPlan(@RequestBody WorkoutPlan workoutPlan) {
        return Result.success(workoutService.createWorkoutPlan(workoutPlan));
    }

    @GetMapping("/plan")
    public Result<List<WorkoutPlan>> getPlans(@RequestParam Long userId) {
        return Result.success(workoutService.getAllWorkoutPlans(userId));
    }

    @GetMapping("/plan/{planId}")
    public Result<WorkoutPlan> getPlanById(@PathVariable Long planId) {
        return Result.success(workoutService.getWorkoutPlanById(planId));
    }

    @PutMapping("/plan/{planId}")
    public Result<WorkoutPlan> updatePlan(@PathVariable Long planId, @RequestBody WorkoutPlan workoutPlan) {
        return Result.success(workoutService.updateWorkoutPlan(planId, workoutPlan));
    }

    @DeleteMapping("/plan/{planId}")
    public Result<Void> deletePlan(@PathVariable Long planId) {
        workoutService.deleteWorkoutPlan(planId);
        return Result.success();
    }

}
