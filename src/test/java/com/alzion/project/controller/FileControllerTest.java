package com.alzion.project.controller;

import com.alzion.project.exception.IncorrectFileSizeException;
import com.alzion.project.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private FileService fileService;
    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
    }

    @Test
    void testUploadFileToLocalStorageExceedsFileSizeLimit() {
        // 1MB in bytes
        long maxFileSize = 1000000;
        MockMultipartFile largeFile = new MockMultipartFile(
                "file", "large_test_file.txt", "text/plain", new byte[(int) maxFileSize + 1]);
        String passcode = "testPasscode";

        IncorrectFileSizeException exception = assertThrows(
                IncorrectFileSizeException.class,
                () -> fileController.uploadFileToLocalStorage(largeFile, passcode)
        );
        assertEquals("File Size exceeds the limit of " + maxFileSize + " bytes", exception.getMessage());
    }

    @Test
    void testUploadFileToLocalStorageSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());
        String passcode = "secret";
        String uniqueUrl = "uniqueUrl";

        lenient().when(fileService.uploadFile(file, passcode)).thenReturn(uniqueUrl);

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/files/upload/local-storage")
                        .file(file)
                        .param("passcode", passcode)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "File uploaded successfully to local storage. Download URL: http://localhost/files/download/local-storage/"
                ));
    }


    @Test
    void testUploadFileToLocalStorageFailure() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());
        String passcode = "secret";

        lenient().when(fileService.uploadFile(file, passcode)).thenThrow(new RuntimeException("Upload error"));

        ResponseEntity<?> response = fileController.uploadFileToLocalStorage(file, passcode);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("File upload failed.", response.getBody());
    }

}
