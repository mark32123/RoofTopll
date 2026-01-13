package com.ll.rooftopll.entity;

import lombok.Data;

@Data
public class WorkoutActivity {
    private Long id;
    private Long sessionId;
    private Long exerciseId;
    private Integer orderNum;
}