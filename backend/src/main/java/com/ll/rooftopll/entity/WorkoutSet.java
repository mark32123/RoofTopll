package com.ll.rooftopll.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkoutSet {
    private Long id;
    private Long activityId;
    private Integer setIndex;
    private BigDecimal weight;
    private Integer reps;
    private BigDecimal rpe;
}
