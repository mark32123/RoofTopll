package com.ll.rooftopll.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("weight_log")
public class WeightLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private BigDecimal weight;
    private LocalDate logDate;
}
