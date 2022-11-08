package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.forms.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("/")
public class HomeController {
    private final NoteService noteService;
    private final UserService userService;

    public HomeController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(
        @ModelAttribute("noteForm") NoteForm noteForm,
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());
        if (user != null) {
            model.addAttribute("notes", noteService.getUserNotes(user.getUserid()));
        } else {
            model.addAttribute("error", "User does not exist. Please login again.");
        }

        return "home";
    }

    @PostMapping
    public String createItem(
        @ModelAttribute("noteForm") NoteForm noteForm,
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());

        if (user != null) {
            noteService.createNote(noteForm, user.getUserid());
            noteForm.clear();
            model.addAttribute("notes", noteService.getUserNotes(user.getUserid()));
        } else {
            model.addAttribute("error", "User does not exist. Please login again.");
        }

        return "home";
    }
}
