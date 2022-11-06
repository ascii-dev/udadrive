package com.udacity.jwdnd.course1.cloudstorage.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.udacity.jwdnd.course1.cloudstorage.models.User;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User select(String username);

    @Insert(
        "INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES "
        + "(#{username}, #{salt}, #{password}, #{firstname}, #{lastname})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insert(User user);
}
