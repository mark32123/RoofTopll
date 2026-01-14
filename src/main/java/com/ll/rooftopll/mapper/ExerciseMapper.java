package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.Exercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ExerciseMapper {
    // 根据用户ID获取所有可用动作（系统预设 + 该用户自建）
    List<Exercise> selectAvailableExercises(Long userId);

    // 用户自定义动作添加
    int insertExercise(Exercise exercise);
}