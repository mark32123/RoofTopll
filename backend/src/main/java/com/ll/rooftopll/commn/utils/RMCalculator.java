package com.ll.rooftopll.commn.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RMCalculator {

    public static BigDecimal calculateAverageRM(double weight, int reps) {
        if (reps == 1) return BigDecimal.valueOf(weight);

        // 1. Epley
        double epley = weight * (1 + reps / 30.0);
        // 2. Brzycki
        double brzycki = weight * (36.0 / (37.0 - reps));
        // 3. Lombardi
        double lombardi = weight * Math.pow(reps, 0.1);

        double avg = (epley + brzycki + lombardi) / 3.0;
        return new BigDecimal(avg).setScale(2, RoundingMode.HALF_UP);
    }
}