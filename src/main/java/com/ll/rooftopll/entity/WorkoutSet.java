package com.ll.rooftopll.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("workout_set")
public class WorkoutSet {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long activityId;
    private Integer setIndex;
    private BigDecimal weight;
    private Integer reps;
    private BigDecimal rpe;
}
