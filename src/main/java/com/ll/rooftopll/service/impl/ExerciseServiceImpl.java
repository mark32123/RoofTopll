package com.ll.rooftopll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.rooftopll.entity.Exercise;
import com.ll.rooftopll.mapper.ExerciseMapper;
import com.ll.rooftopll.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Override
    public List<Exercise> searchExercises(String name, Long userId) {
        return exerciseMapper.selectList(
                new QueryWrapper<Exercise>()
                        .like("name", name) // 模糊查询
                        .and(wrapper ->
                                wrapper.eq("user_id", 0) // 系统预设
                                        .or()
                                        .eq("user_id", userId) // 该用户自定义
                        )
        );
    }

    @Override
    public List<Exercise> getCommonExercises() {
        // 优先展示三大项
        return exerciseMapper.selectList(
                new QueryWrapper<Exercise>().eq("is_big_three", true)
        );
    }

    @Override
    public void createCustomExercise(Exercise exercise) {
        exerciseMapper.insert(exercise);
    }
}