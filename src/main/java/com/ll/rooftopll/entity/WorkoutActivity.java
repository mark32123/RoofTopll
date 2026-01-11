package com.ll.rooftopll.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("workout_activity")
public class WorkoutActivity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sessionId;
    private Long exerciseId;
    private Integer orderNum; // 动作在当天的排序
}