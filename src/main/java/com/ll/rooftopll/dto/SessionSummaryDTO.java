package com.ll.rooftopll.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SessionSummaryDTO {
    private Long sessionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalVolume; // 总训练量
    private Long durationMinutes;  // 训练时长（分钟）
    private String sessionInspiration; // 总结性激励语
    private List<ActivityDetailDTO> activities; // 包含的具体动作列表
}
