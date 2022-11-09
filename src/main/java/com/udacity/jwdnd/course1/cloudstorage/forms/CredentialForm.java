package com.udacity.jwdnd.course1.cloudstorage.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialForm {
    private Integer credentialId;
    private String url;
    private String username;
    private String password;
    private String encodedKey;

    public void clear() {
        this.credentialId = 0;
        this.url = "";
        this.username = "";
        this.password = "";
        this.encodedKey = "";
    } 
}
