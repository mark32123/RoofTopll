package com.ll.rooftopll.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SetDetailDTO {
    private Integer setIndex;
    private BigDecimal weight;
    private Integer reps;
}
