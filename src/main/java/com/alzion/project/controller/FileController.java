package com.alzion.project.controller;

import com.alzion.project.exception.IncorrectFileSizeException;
import com.alzion.project.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    Logger log = LoggerFactory.getLogger(FileController.class);

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload/local-storage")
    public ResponseEntity<?> uploadFileToLocalStorage(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("passcode") String passcode) {
        log.info("Inside uploadFileToLocalStorage Controller");
        long maxFileSize = 1000000;        // bytes
        if(file.getSize() > maxFileSize) {
            throw new IncorrectFileSizeException(String.format("File Size exceeds the limit of " + maxFileSize + " bytes"));
        }
        try {
            String uniqueUrl = fileService.uploadFile(file, passcode);
            String downloadUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/download/local-storage/")
                    .path(uniqueUrl)
                    .toUriString();
            return ResponseEntity.ok().body("File uploaded successfully to local storage. Download URL: " + downloadUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
        }
    }

    @GetMapping("/download/local-storage/{uniqueUrl}")
    public ResponseEntity<?> downloadFileFromLocalStorage(@PathVariable String uniqueUrl,
                                                          @RequestParam("passcode") String passcode) {
        log.info("Inside downloadFileFromLocalStorage Controller");
        byte[] decryptedFile = fileService.downloadFile(uniqueUrl, passcode);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uniqueUrl)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(decryptedFile);
    }

}

