package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.forms.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(
        CredentialMapper credentialMapper,
        EncryptionService encryptionService
    ) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void createCredential(
        CredentialForm credentialForm,
        Integer userId
    ) {
        credentialMapper.insert(
            new Credential(
                null,
                credentialForm.getUrl(),
                credentialForm.getUsername(),
                credentialForm.getEncodedKey(),
                credentialForm.getPassword(),
                userId
            )
        );
    }

    public List<Credential> getUserCredentials(int userId) {
        List<Credential> credentials = credentialMapper.selectALLByUser(userId);
        List<Credential> output = new ArrayList<>();

        for (Credential credential : credentials) {
            credential.setPassword(
                encryptionService.decryptValue(
                    credential.getPassword(),
                    credential.getKey()
                )
            );

            output.add(credential);
        }

        return output;
    }

    public Credential getCredential(int credentialId) {
        return credentialMapper.select(credentialId);
    }

    public boolean updateCredential(Credential credential, int userId) {
        if (credential.getUserid() != userId) {
            return false;
        }

        return credentialMapper.update(credential);
    }

    public boolean deleteCredentialById(int credentialId, int userId) {
        Credential credential = credentialMapper.select(credentialId);
        if (
            credential != null 
            && credential.getUserid() == userId 
            && credentialMapper.delete(credentialId)
        ) {
            return true;
        }

        return false;
    }
}
