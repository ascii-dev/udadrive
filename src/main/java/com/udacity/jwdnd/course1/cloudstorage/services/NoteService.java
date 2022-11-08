package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.forms.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getUserNotes(int userId) {
        return noteMapper.selectALLByUser(userId);
    }

    public void createNote(NoteForm noteForm, int userId) {
        noteMapper.insert(
            new Note(
                null,
                noteForm.getNoteTitle(),
                noteForm.getNoteDescription(),
                userId
            )
        );
    }
}
