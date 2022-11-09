package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.security.Principal;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("files")
public class FilesController {
    private final UserService userService;

    public FilesController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserNotes(
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());

        if (user == null) {
            model.addAttribute("error", "User does not exist. Please login again.");
        }

        return "files";
    }
}
