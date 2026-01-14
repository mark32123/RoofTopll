package com.ll.rooftopll.controller;

import com.ll.rooftopll.commn.api.Result;
import com.ll.rooftopll.entity.Exercise;
import com.ll.rooftopll.mapper.ExerciseMapper;
import com.ll.rooftopll.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseMapper exerciseMapper;

    @GetMapping("/list")
    public Result<List<Exercise>> list(@RequestParam(defaultValue = "0") Long userId) {
        return Result.success(exerciseMapper.selectAvailableExercises(userId));
    }
}
