package com.udacity.jwdnd.course1.cloudstorage.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> selectALLByUser(int userId);

    @Insert(
        "INSERT INTO CREDENTIALS (url, username, key, password, userid) "
        + " VALUES (#{url}, #{username}, #{key}, #{password}, #{userid})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insert(Credential credential);

    @Update(
        "UPDATE CREDENTIALS SET url = #{url}, username = #{username}, "
        + "key = #{key}, password = #{password} WHERE credentialid = #{credentialid}"
    )
    boolean update(Credential credential);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    Credential select(int credentialid);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    boolean delete(int credentialid);
}
