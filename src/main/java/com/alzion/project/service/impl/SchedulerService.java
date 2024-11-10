package com.alzion.project.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Stream;

@Service
public class SchedulerService {
    Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private static final Path FILE_DIRECTORY = Paths.get("src\\main\\resources\\uploads");
    private static final Duration FILE_EXPIRATION = Duration.ofHours(48);

    @Scheduled(fixedRate = 3600000)  // Run every 1 hour
    public void deleteOldFiles() throws IOException {
        log.info("Scheduler Started for deleting files from local storage");
        if (Files.exists(FILE_DIRECTORY)) {
            try(Stream<Path> list = Files.list(FILE_DIRECTORY)) {
                list.filter(Files::isRegularFile)
                        .filter(this::isFileOlderThan24Hours)
                        .forEach(this::deleteFile);
            }
        }
    }

    boolean isFileOlderThan24Hours(Path file) {
        log.info("Inside isFileOlderThan24Hours method");
        try {
            Instant fileLastModified = Files.getLastModifiedTime(file).toInstant();
            Instant expirationTime = Instant.now().minus(FILE_EXPIRATION);
            return fileLastModified.isBefore(expirationTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    void deleteFile(Path file) {
        log.info("Inside deleteFile method");
        try {
            Files.delete(file);
            log.info("Deleted old file from local storage: " + file);
        } catch (IOException e) {
            log.error("Failed to delete file from local storage: " + file);
            log.debug(String.format("Failed to delete file, exception occured : " + Arrays.toString(e.getStackTrace())));
        }
    }
}

