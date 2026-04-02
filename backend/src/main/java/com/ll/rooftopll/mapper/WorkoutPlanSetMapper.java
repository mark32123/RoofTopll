package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.WorkoutPlanSet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkoutPlanSetMapper {
    List<WorkoutPlanSet> findByActivityId(Long activityId);

    void insert(WorkoutPlanSet set);

    void deleteByActivityIds(List<Long> activityIds);
}
