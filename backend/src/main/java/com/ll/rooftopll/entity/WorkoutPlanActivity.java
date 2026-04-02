package com.ll.rooftopll.entity;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutPlanActivity {
    private Long id;
    private Long planId;
    private Long exerciseId;
    private Integer orderNum;
    private List<WorkoutPlanSet> sets = new java.util.ArrayList<>();
}
