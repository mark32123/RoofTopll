package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.WorkoutSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WorkoutSetMapper {
    // 添加组数记录
    int insertSet(WorkoutSet set);

    /**
     * 获取三大项单项的最大重量变化 (核心功能)
     * @param exerciseId 对应深蹲、卧推或硬拉的ID
     */
    List<Map<String, Object>> getBigThreeTrend(@Param("userId") Long userId,
                                               @Param("exerciseId") Long exerciseId);
}