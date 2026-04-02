package com.ll.rooftopll.service;

import com.ll.rooftopll.entity.Exercise;
import java.util.List;

public interface ExerciseService {
    List<Exercise> getAvailableExercises(Long userId); // 刚才 Controller 调用的方法
    List<Exercise> searchExercises(String name, Long userId);
    List<Exercise> getCommonExercises();
    void createCustomExercise(Exercise exercise);
}
