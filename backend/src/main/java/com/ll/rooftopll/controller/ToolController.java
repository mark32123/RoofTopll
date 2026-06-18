package com.ll.rooftopll.controller;

import com.ll.rooftopll.commn.api.Result;
import com.ll.rooftopll.entity.RMResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tool")
public class ToolController {

    @GetMapping("/rm-calc")
    public Result<RMResponse> calculateRM(@RequestParam Double weight, @RequestParam Integer reps) {
        if (reps <= 0) return Result.error("次数必须大于0哦");

        double epley = weight * (1 + reps / 30.0);
        double brzycki = weight * (36.0 / (37.0 - reps));
        double lombardi = weight * Math.pow(reps, 0.1);
        double average = (epley + brzycki + lombardi) / 3.0;

        RMResponse res = new RMResponse();
        res.setEpley(BigDecimal.valueOf(epley).setScale(2, RoundingMode.HALF_UP));
        res.setBrzycki(BigDecimal.valueOf(brzycki).setScale(2, RoundingMode.HALF_UP));
        res.setLombardi(BigDecimal.valueOf(lombardi).setScale(2, RoundingMode.HALF_UP));
        res.setAverage(BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP));

        return Result.success(res);
    }
}