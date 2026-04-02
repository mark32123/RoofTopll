package com.ll.rooftopll.entity;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutPlan {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private java.time.LocalDateTime createdAt;
    private List<WorkoutPlanActivity> activities = new java.util.ArrayList<>();
}
