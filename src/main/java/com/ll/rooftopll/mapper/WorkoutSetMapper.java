package com.ll.rooftopll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.rooftopll.entity.WorkoutSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface WorkoutSetMapper extends BaseMapper<WorkoutSet> {

    @Select("SELECT DATE(ws.start_time) as training_date, e.name as exercise_name, " +
            "st.weight, st.reps " +
            "FROM workout_set st " +
            "JOIN workout_activity wa ON st.activity_id = wa.id " +
            "JOIN workout_session ws ON wa.session_id = ws.id " +
            "JOIN exercise e ON wa.exercise_id = e.id " +
            "WHERE ws.user_id = #{userId} AND e.is_big_three = 1 " +
            "ORDER BY ws.start_time ASC")
    List<Map<String, Object>> selectBigThreeHistory(Long userId);


    @Select("SELECT SUM(st.weight * st.reps) " +
            "FROM workout_set st " +
            "JOIN workout_activity wa ON st.activity_id = wa.id " +
            "JOIN workout_session ws ON wa.session_id = ws.id " +
            "WHERE ws.user_id = #{userId}")
    Double sumTotalVolumeByUserId(Long userId);
}
