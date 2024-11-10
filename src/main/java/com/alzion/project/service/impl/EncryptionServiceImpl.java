package com.alzion.project.service.impl;

import com.alzion.project.service.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    Logger log = LoggerFactory.getLogger(EncryptionServiceImpl.class);
    private static final String ALGORITHM = "AES";

    @Override
    public byte[] encrypt(byte[] data, String passcode) {
        log.info("Inside encrypt method");
        SecretKeySpec key = getKey(passcode);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] data, String passcode) {
        log.info("Inside decrypt method");
        SecretKeySpec key = getKey(passcode);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    private static SecretKeySpec getKey(String passcode) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] key = sha.digest(passcode.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key, ALGORITHM);
    }
}

