package com.alzion.project.service.impl;

import com.alzion.project.exception.InvalidPassCodeException;
import com.alzion.project.service.EncryptionService;
import com.alzion.project.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final EncryptionService encryptionService;
    private final String directory = "src\\main\\resources\\uploads\\";

    public FileServiceImpl(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public String uploadFile(MultipartFile file, String passcode) {
        log.info("Inside uploadFile method");
        // Generate unique URL
        String uniqueUrl = UUID.randomUUID().toString();
        String[] extension;
        try {
            // Encrypt the file with the passcode
            byte[] encryptedData = encryptionService.encrypt(file.getBytes(), passcode);
            // Store the encrypted file in a safe location
            extension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
            uniqueUrl = uniqueUrl + "." + extension[1];
            try (FileOutputStream fileOutputStream = new FileOutputStream(directory + uniqueUrl)) {
                fileOutputStream.write(encryptedData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return uniqueUrl;
    }

    @Override
    public byte[] downloadFile(String uniqueUrl, String passcode) {
        log.info("Inside downloadFile method");
        if(null == uniqueUrl || uniqueUrl.equals(" ")) {
            throw new NullPointerException("uniqueUrl cannot be null or empty");
        }
        byte[] result;
        try {
            byte[] encryptedData = Files.readAllBytes(Paths.get(directory + uniqueUrl));
            result = encryptionService.decrypt(encryptedData, passcode);
        } catch (RuntimeException e) {
            throw new InvalidPassCodeException("Invalid passcode");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
