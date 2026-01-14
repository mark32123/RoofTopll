package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.WorkoutSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WorkoutSessionMapper {

    WorkoutSession findActiveSession(Long userId);

    // 插入并返回 ID
    int insert(WorkoutSession session);
}
