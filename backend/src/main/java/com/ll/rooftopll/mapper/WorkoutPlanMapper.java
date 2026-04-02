package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.WorkoutPlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkoutPlanMapper {
    List<WorkoutPlan> findByUserId(Long userId);

    WorkoutPlan findById(Long planId);

    void insert(WorkoutPlan workoutPlan);

    void update(WorkoutPlan workoutPlan);

    void delete(Long planId);
}
