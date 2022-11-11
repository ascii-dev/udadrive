package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("files")
public class FilesController {
    private final UserService userService;
    private final FileService fileService;

    public FilesController(
        UserService userService,
        FileService fileService
    ) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping
    public String geUserFiles(
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());

        if (user == null) {
            model.addAttribute(
                "error",
                "User does not exist. Please login again."
            );
            return "redirect:login";
        }

        model.addAttribute(
            "files",
            fileService.getAllFiles(user.getUserid())
        );

        return "files";
    }

    @PostMapping
    public String uploadFile(
        @RequestParam("fileUpload") MultipartFile multipartFile,
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());
        if (user == null) {
            model.addAttribute(
                "error",
                "User does not exist. Please login again."
            );
            return "redirect:login";
        }

        try {
            if (multipartFile.getBytes().length == 0) {
                model.addAttribute(
                    "error", 
                    "File can not be empty."
                );
            } else {
                fileService.uploadFile(multipartFile, user.getUserid());
            }

        } catch (IOException exception) {
            model.addAttribute(
                "error",
                "File upload failed, please try again."
            );
        }

        model.addAttribute(
            "files",
            fileService.getAllFiles(user.getUserid())
        );

        return "files";
    }

    @GetMapping("/download/{fileId}")
    public void downloadFile(
        @PathVariable int fileId,
        HttpServletResponse response
    ) throws IOException {
        File file = fileService.getSingleFile(fileId);

        response.setContentType(file.getContenttype());
        response.setContentLength(Integer.parseInt(file.getFilesize()));
        response.setHeader("Content-Disposition", "file:" + file.getFilename());
        response.getOutputStream().write(file.getFiledata());
        response.flushBuffer();
    }

    @PostMapping("/delete")
    public String deleteFile(
        @RequestParam("fileId") int fileId,
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());
        if (user == null) {
            model.addAttribute(
                "error",
                "User does not exist. Please login again."
            );
            return "redirect:login";
        }

        boolean deleted = fileService.deleteFile(fileId);
        if (!deleted) {
            model.addAttribute(
                "error", 
                "File could not be deleted"
            );
        }

        model.addAttribute(
            "files",
            fileService.getAllFiles(user.getUserid())
        );

        return "files";
    }
}
