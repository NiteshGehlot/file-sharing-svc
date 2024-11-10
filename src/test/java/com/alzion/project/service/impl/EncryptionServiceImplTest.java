package com.alzion.project.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.crypto.spec.SecretKeySpec;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptionServiceImplTest {
    @InjectMocks
    private EncryptionServiceImpl encryptionService;

    @BeforeEach
    void setUp() {
        // Initialize the mock objects before each test
        encryptionService = new EncryptionServiceImpl();
    }

    @Test
    void testEncrypt() {
        // Sample data and passcode
        String data = "Test data";
        String passcode = "mysecretkey";
        byte[] inputData = data.getBytes();

        // Encrypt the data
        byte[] encryptedData = encryptionService.encrypt(inputData, passcode);

        // Ensure that the encrypted data is not null or the same as the original data
        assertNotNull(encryptedData);
        assertNotEquals(new String(inputData), new String(encryptedData));

        // Optionally, check if the encryption process is functioning properly by inspecting logs (depending on the use case)
    }

    @Test
    void testDecrypt() {
        // Sample data and passcode
        String data = "Test data";
        String passcode = "mysecretkey";
        byte[] inputData = data.getBytes();

        // Encrypt the data first
        byte[] encryptedData = encryptionService.encrypt(inputData, passcode);

        // Now decrypt the data
        byte[] decryptedData = encryptionService.decrypt(encryptedData, passcode);

        // Ensure the decrypted data matches the original data
        assertNotNull(decryptedData);
        assertEquals(new String(inputData), new String(decryptedData));
    }

    @Test
    void testDecryptWithIncorrectPasscode() {
        // Sample data and passcodes
        String data = "Test data";
        String correctPasscode = "mysecretkey";
        String wrongPasscode = "wrongkey";
        byte[] inputData = data.getBytes();

        // Encrypt the data using the correct passcode
        byte[] encryptedData = encryptionService.encrypt(inputData, correctPasscode);

        // Try decrypting with an incorrect passcode, expecting a RuntimeException
        assertThrows(RuntimeException.class, () -> {
            encryptionService.decrypt(encryptedData, wrongPasscode);
        });
    }
    @Test
    void testEncryptWithNullPasscode() {
        // Test with null passcode
        String data = "Test data";
        assertThrows(NullPointerException.class, () -> encryptionService.encrypt(data.getBytes(), null));
    }

    @Test
    void testDecryptWithNullPasscode() {
        // Test with null passcode
        String data = "Test data";
        assertThrows(NullPointerException.class, () -> encryptionService.decrypt(data.getBytes(), null));
    }
}
