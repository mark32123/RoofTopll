package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.WeightLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WeightLogMapper {
    // 记录/更新今日体重 (利用唯一索引实现upsert)
    int upsertWeight(WeightLog weightLog);

    // 获取体重历史数据 (用于前端画曲线图)
    List<WeightLog> selectWeightHistory(@Param("userId") Long userId);
}