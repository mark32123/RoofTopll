package com.ll.rooftopll.mapper;

import com.ll.rooftopll.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    int insertUser(User user);
    int updateAvatar(@Param("id") Long id, @Param("avatar") String avatar);
}