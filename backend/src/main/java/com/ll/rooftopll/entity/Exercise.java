package com.ll.rooftopll.entity;

import lombok.Data;

@Data
public class Exercise {
    private Long id;
    private String name;
    private String category;
    private Boolean isBigThree; // 是否属于三大项
    private Long userId; // 0为系统预设，其他为用户自定义
}