package com.ll.rooftopll.service.impl;

import com.ll.rooftopll.entity.Exercise;
import com.ll.rooftopll.mapper.ExerciseMapper;
import com.ll.rooftopll.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {


    @Override
    public List<Exercise> searchExercises(String name, Long userId) {
        return List.of();
    }

    @Override
    public List<Exercise> getCommonExercises() {
        return List.of();
    }

    @Override
    public void createCustomExercise(Exercise exercise) {

    }
}