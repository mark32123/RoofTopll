package com.ll.rooftopll.dto;

import com.ll.rooftopll.entity.WorkoutSet;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutActivityDTO {
    private Long sessionId;   // 属于哪次训练
    private Long exerciseId;  // 哪个动作
    private List<WorkoutSet> sets; // 这一组里所有的重量次数信息
}
