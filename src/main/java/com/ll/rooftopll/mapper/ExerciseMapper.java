package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.Exercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ExerciseMapper {
    // 获取系统预设动作(user_id=0)和该用户的自定义动作
    List<Exercise> selectAllAvailable(@Param("userId") Long userId);

    // 用户自定义动作添加
    int insertExercise(Exercise exercise);
}