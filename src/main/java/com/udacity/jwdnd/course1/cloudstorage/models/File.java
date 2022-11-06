package com.udacity.jwdnd.course1.cloudstorage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private Integer fileid;
    private String filename;
    private String contenttype;
    private String filesize;
    private Integer userid;
    private byte[] filedata;
}
