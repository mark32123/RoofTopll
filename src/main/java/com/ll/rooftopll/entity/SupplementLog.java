package com.ll.rooftopll.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("supplement_log")
public class SupplementLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sessionId;
    private String name;
    private String dosage;      //剂量信息
    private LocalDateTime takenTime; //服用时间
}
