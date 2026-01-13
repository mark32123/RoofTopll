package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.SupplementLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplementLogMapper {
    // 记录补剂使用
    int insertLog(SupplementLog log);

    // 查询某次训练课相关的补剂使用
    List<SupplementLog> selectBySessionId(@Param("sessionId") Long sessionId);
}