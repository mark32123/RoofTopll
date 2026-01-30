package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.SupplementLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SupplementLogMapper {

    @Insert("INSERT INTO supplement_log (session_id, name, dosage, taken_time) " +
            "VALUES (#{sessionId}, #{name}, #{dosage}, #{takenTime})")
    int insert(SupplementLog log);

    List<SupplementLog> selectBySessionId(Long sessionId);
}