package com.ll.rooftopll.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkoutPlanSet {
    private Long id;
    private Long planActivityId;
    private Integer setIndex;
    private BigDecimal weight;
    private Integer reps;
}
