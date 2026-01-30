package com.ll.rooftopll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupplementTemplateDTO {
    private String name;    // 补剂名称
    private String unit;    // 默认单位：勺、g、mg
}
