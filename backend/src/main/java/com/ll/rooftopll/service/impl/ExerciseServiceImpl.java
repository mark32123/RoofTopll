package com.ll.rooftopll.service.impl;

import com.ll.rooftopll.entity.Exercise;
import com.ll.rooftopll.mapper.ExerciseMapper;
import com.ll.rooftopll.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // 👈 注解在这里！

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Override
    public List<Exercise> getAvailableExercises(Long userId) {
        return exerciseMapper.selectAvailableExercises(userId);
    }

    // 搜索动作
    public List<Exercise> searchExercises(String name, Long userId) {
        // 以后这里可以写：name = "%" + name + "%" 这种模糊查询逻辑
        return exerciseMapper.searchExercises(name, userId);
    }

    // 获取常用动作
    public List<Exercise> getCommonExercises() {
        return exerciseMapper.selectCommon();
    }

    @Override
    public void createCustomExercise(Exercise exercise) {
        exerciseMapper.insert(exercise);
    }

    @Override
    public void deleteExercise(Long id) {
        exerciseMapper.deleteById(id);
    }
}