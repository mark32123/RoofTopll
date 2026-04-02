package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.WorkoutActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
public interface WorkoutActivityMapper {

    WorkoutActivity findActivity(Long sessionId, Long exerciseId);

    int insert(WorkoutActivity activity);

    int countBySessionId(@Param("sessionId") Long sessionId);
}