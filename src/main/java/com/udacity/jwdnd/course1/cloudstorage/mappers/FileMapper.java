package com.udacity.jwdnd.course1.cloudstorage.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.udacity.jwdnd.course1.cloudstorage.models.File;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getAllUserFiles(int userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getSingleFile(int fileId);

    @Insert(
        "INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) "
        + " VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    boolean delete(int fileId);
}
