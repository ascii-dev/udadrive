package com.udacity.jwdnd.course1.cloudstorage.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }
    
    public int uploadFile(MultipartFile multipartFile, int userId) throws IOException {
        File file = new File();
        file.setFilename(multipartFile.getOriginalFilename());
        file.setFilesize(String.valueOf(multipartFile.getSize()));
        file.setContenttype(multipartFile.getContentType());
        file.setFiledata(multipartFile.getBytes());
        file.setUserid(userId);
        return fileMapper.insert(file);
    }

    public List<File> getAllFiles(int userId) {
        return fileMapper.getAllUserFiles(userId);
    }

    public File getSingleFile(int fileId) {
        return fileMapper.getSingleFile(fileId);
    }

    public boolean deleteFile(int fileId) {
        return fileMapper.delete(fileId);
    }
}
