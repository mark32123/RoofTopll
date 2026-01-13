package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.WorkoutSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WorkoutSessionMapper {
    // 开启一次训练
    int insertSession(WorkoutSession session);

    // 结束训练，更新结束时间和总容量
    int finishSession(@Param("id") Long id,
                      @Param("endTime") LocalDateTime endTime,
                      @Param("totalVolume") BigDecimal totalVolume);

    // 获取某用户最近的训练记录
    List<WorkoutSession> selectRecentSessions(@Param("userId") Long userId);
}
