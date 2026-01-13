package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.WorkoutActivity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkoutActivityMapper {
    // 为当前训练课添加动作
    int insertActivity(WorkoutActivity activity);
}