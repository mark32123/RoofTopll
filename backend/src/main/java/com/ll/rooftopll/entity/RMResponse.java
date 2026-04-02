package com.ll.rooftopll.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RMResponse {
    private BigDecimal epley;
    private BigDecimal brzycki;
    private BigDecimal lombardi;
    private BigDecimal average;
}
