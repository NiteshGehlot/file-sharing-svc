package com.alzion.project.service;

public interface EncryptionService {
    byte[] encrypt(byte[] data, String passcode);
    byte[] decrypt(byte[] data, String passcode);
}
