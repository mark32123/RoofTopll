package com.ll.rooftopll.service;

import com.ll.rooftopll.entity.Exercise;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExerciseService {
    // 根据名称模糊搜索动作（例如输入“深蹲”，返回所有深蹲相关动作）
    List<Exercise> searchExercises(String name, Long userId);

    // 获取常用的动作列表（比如三大项）
    List<Exercise> getCommonExercises();

    // 用户自定义新动作
    void createCustomExercise(Exercise exercise);
}
