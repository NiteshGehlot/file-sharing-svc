package com.alzion.project.service.impl;

import com.alzion.project.service.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private MultipartFile file;

    private final String passcode = "mysecretkey";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadFileSuccess() throws IOException {
        // Prepare test data
        byte[] fileData = "Test file content".getBytes();
        when(file.getBytes()).thenReturn(fileData);
        when(file.getOriginalFilename()).thenReturn("testfile.txt");

        byte[] encryptedData = "Encrypted data".getBytes();
        when(encryptionService.encrypt(fileData, passcode)).thenReturn(encryptedData);

        // Perform upload
        String uniqueUrl = fileService.uploadFile(file, passcode);

        // Verify that the encrypted file is written to disk
        Path filePath = Path.of("src/main/resources/uploads/" + uniqueUrl);
        assertTrue(Files.exists(filePath)); // File should be created
        Files.delete(filePath); // Cleanup after test
    }

    @Test
    void testUploadFileFailureEncryption() throws IOException {
        // Prepare test data
        byte[] fileData = "Test file content".getBytes();
        when(file.getBytes()).thenReturn(fileData);
        when(file.getOriginalFilename()).thenReturn("testfile.txt");

        // Simulate encryption failure
        when(encryptionService.encrypt(fileData, passcode)).thenThrow(new RuntimeException("Encryption error"));

        // Test that an exception is thrown
        assertThrows(RuntimeException.class, () -> fileService.uploadFile(file, passcode));
    }

    @Test
    void testDownloadFileSuccess() throws IOException {
        // Prepare test data
        String uniqueUrl = "f3668994-cf74-41d7-aee6-5bbda0128fcb.pdf";
        byte[] encryptedData = "Encrypted file content".getBytes();
        byte[] decryptedData = "Decrypted file content".getBytes();

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllBytes(Path.of("src/main/resources/uploads/" + uniqueUrl)))
                    .thenReturn(encryptedData);
            when(encryptionService.decrypt(encryptedData, passcode)).thenReturn(decryptedData);

            // Perform download
            byte[] result = fileService.downloadFile(uniqueUrl, passcode);

            // Verify result
            assertArrayEquals(decryptedData, result);
        }
    }


    @Test
    void testUploadFileNullFile() {
        // Test when file is null
        assertThrows(NullPointerException.class, () -> fileService.uploadFile(null, passcode));
    }

    @Test
    void testDownloadFileNullFile() {
        // Test when file is null
        assertThrows(NullPointerException.class, () -> fileService.downloadFile(null, passcode));
    }
}
