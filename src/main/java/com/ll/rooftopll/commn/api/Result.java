package com.ll.rooftopll.commn.api;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    // 全参构造函数
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 1. 成功：不带数据 (用于增删改)
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 2. 成功：带数据 (用于查询)
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 3. 失败：自定义错误
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }
}