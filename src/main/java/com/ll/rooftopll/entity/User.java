package com.ll.rooftopll.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;

    @JsonIgnore // 返回用户信息时隐藏密码
    private String password;
    private String avatar;
    private Integer gender; // 0:未知, 1:男, 2:女
}
