package com.udacity.jwdnd.course1.cloudstorage.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteForm {
    private Integer noteId;
    private String noteTitle;
    private String noteDescription;

    public void clear() {
        this.noteId = 0;
        this.noteTitle = "";
        this.noteDescription = "";
    }
}
