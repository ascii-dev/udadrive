package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.forms.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("notes")
public class NotesController {
    private final NoteService noteService;
    private final UserService userService;

    public NotesController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    public String getUserNotes(
        @ModelAttribute("noteForm") NoteForm noteForm,
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());

        if (user == null) {
            model.addAttribute("error", "User does not exist. Please login again.");
            return "redirect:login";
        }
        model.addAttribute("notes", noteService.getUserNotes(user.getUserid()));

        return "notes";
    }

    @PostMapping
    public String createOrUpdate(
        @ModelAttribute("noteForm") NoteForm noteForm,
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());

        if (user == null) {
            model.addAttribute("error", "User does not exist. Please login again.");
            return "redirect:login";
        }

        if (noteForm.getNoteId() == null) {
            noteService.createNote(noteForm, user.getUserid());
        } else {
            Note note = noteService.getNote(noteForm.getNoteId());
            note.setNotetitle(noteForm.getNoteTitle());
            note.setNotedescription(noteForm.getNoteDescription());

            boolean updated = noteService.updateNote(note, user.getUserid());
            if (!updated) {
                model.addAttribute("error", "Note can not be updated");
            }
        }
        noteForm.clear();
        model.addAttribute("notes", noteService.getUserNotes(user.getUserid()));

        return "notes";
    }

    @PostMapping("/delete")
    public String deleteNote(
        @ModelAttribute("noteForm") NoteForm noteForm,
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());

        if (user == null) {
            model.addAttribute("error", "User does not exist. Please login again.");
            return "redirect:login";
        }

        boolean deleted = noteService.deleteNoteById(noteForm.getNoteId(), user.getUserid());
        if (!deleted) {
            model.addAttribute("error", "Note can not be deleted");
        }
        noteForm.clear();
        model.addAttribute("notes", noteService.getUserNotes(user.getUserid()));

        return "notes";
    }
}
