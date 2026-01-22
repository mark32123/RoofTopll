package com.ll.rooftopll.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BigThreeDTO {
    private LocalDate recordDate; // 训练日期
    private BigDecimal squat;      // 深蹲最大重量
    private BigDecimal bench;      // 卧推最大重量
    private BigDecimal deadlift;   // 硬拉最大重量
    private BigDecimal total;      // 三项总和
}