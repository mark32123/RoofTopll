package com.ll.rooftopll.commn.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormulaUtils {

    /**
     * Epley公式计算1RM
     * 1RM = Weight * (1 + Reps/30.0)
     */
    public static BigDecimal calculateOneRM(BigDecimal weight, Integer reps) {
        if (reps == 1) return weight;
        if (reps <= 0) return BigDecimal.ZERO;

        BigDecimal factor = BigDecimal.valueOf(1 + reps / 30.0);
        return weight.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}
