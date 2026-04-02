package com.ll.rooftopll.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupplementLog {
    private Long id;
    private Long sessionId;
    private String name;
    private String dosage;
    private LocalDateTime takenTime;
}