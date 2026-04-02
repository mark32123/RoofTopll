package com.ll.rooftopll.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WeightLog {
    private Long id;
    private Long userId;
    private BigDecimal weight;
    private LocalDate logDate;
    private LocalDateTime createTime;
}
