package com.udacity.jwdnd.course1.cloudstorage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private Integer noteid;
    private String notetitle;
    private String notedescription;
    private Integer userid;
}
