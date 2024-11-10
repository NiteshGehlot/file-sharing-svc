package com.alzion.project.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(MultipartFile file, String passcode);
    byte[] downloadFile(String uniqueUrl, String passcode);
}

