package com.udacity.jwdnd.course1.cloudstorage.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> selectALLByUser(int userId);

    @Insert(
        "INSERT INTO NOTES (notetitle, notedescription, userid) "
        + " VALUES (#{notetitle}, #{notedescription}, #{userid})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insert(Note note);
}
