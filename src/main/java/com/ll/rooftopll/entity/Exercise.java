package com.ll.rooftopll.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("exercise")
public class Exercise {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String category;
    private Boolean isBigThree; // 是否属于三大项
    private Long userId; // 0为系统预设，其他为用户自定义
}