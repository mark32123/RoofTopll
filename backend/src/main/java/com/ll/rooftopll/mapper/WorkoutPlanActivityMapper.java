package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.WorkoutPlanActivity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkoutPlanActivityMapper {
    List<WorkoutPlanActivity> findByPlanId(Long planId);

    void insert(WorkoutPlanActivity activity);

    void deleteByPlanId(Long planId);
}
