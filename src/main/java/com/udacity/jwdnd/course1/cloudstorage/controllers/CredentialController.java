package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.forms.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("credentials")
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public CredentialController(
        UserService userService,
        CredentialService credentialService,
        EncryptionService encryptionService
    ) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getUserCredentials(
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());

        if (user == null) {
            model.addAttribute("error", "User does not exist. Please login again.");
        } else {
            model.addAttribute(
                "credentials",
                credentialService.getUserCredentials(user.getUserid())
            );
        }

        return "credentials";
    }

    @PostMapping("/delete")
    public String delete(
        @ModelAttribute("credentialForm") CredentialForm credentialForm,
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());

        if (user == null) {
            model.addAttribute("error", "User does not exist. Please login again");
        } else {
            boolean deleted = credentialService.deleteCredentialById(
                credentialForm.getCredentialId(),
                user.getUserid()
            );
            if (!deleted) {
                model.addAttribute("error", "Credential can not be deleted");
            }
            credentialForm.clear();
            model.addAttribute(
                "credentials",
                credentialService.getUserCredentials(user.getUserid())
            );
        }

        return "credentials";
    }

    @PostMapping
    public String createOrUpdate(
        @ModelAttribute("credentialForm") CredentialForm credentialForm,
        Model model,
        Principal principal
    ) {
        User user = userService.select(principal.getName());
        String encodedKey = encryptionService.getEncodedKey();

        if (user == null) {
            model.addAttribute("error", "User does not exist. Please login again");
        } else {
            if (credentialForm.getCredentialId() == null) {
                credentialForm.setPassword(
                    encryptionService.encryptValue(
                        credentialForm.getPassword(), 
                        encodedKey
                    )
                );
                credentialForm.setEncodedKey(encodedKey);

                credentialService.createCredential(
                    credentialForm, 
                    user.getUserid()
                );
            } else {
                Credential credential = credentialService.getCredential(
                    credentialForm.getCredentialId()
                );
                credential.setUrl(credentialForm.getUrl());
                credential.setUsername(credentialForm.getUsername());
                credential.setPassword(
                    encryptionService.encryptValue(
                        credentialForm.getPassword(),
                        encodedKey
                    )
                );
                credential.setKey(encodedKey);

                boolean updated = credentialService.updateCredential(
                    credential,
                    user.getUserid()
                );
                if (!updated) {
                    model.addAttribute("error", "Credential can not be updated");
                }
            }
            credentialForm.clear();
            model.addAttribute(
                "credentials",
                credentialService.getUserCredentials(user.getUserid())
            );
        }

        return "credentials";
    }
}
