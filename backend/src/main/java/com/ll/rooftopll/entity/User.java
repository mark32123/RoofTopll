package com.ll.rooftopll.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String avatar;
    private Integer gender; // 0:未知, 1:男, 2:女
    private LocalDateTime createTime;
}
