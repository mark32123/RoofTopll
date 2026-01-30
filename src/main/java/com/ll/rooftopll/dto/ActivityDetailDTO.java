package com.ll.rooftopll.dto;

import lombok.Data;

import java.util.List;

@Data
public class ActivityDetailDTO {
    private String exerciseName;
    private List<SetDetailDTO> sets;
    private String achievementTag; // 动作进步标签，例如 "🔥 突破记录"
}
