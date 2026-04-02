package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.WorkoutSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WorkoutSetMapper {
    int insert(WorkoutSet workoutSet);

    int countByActivityId(@Param("activityId") Long activityId);
}