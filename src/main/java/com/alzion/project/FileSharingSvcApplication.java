package com.alzion.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.alzion.project")
@EnableScheduling
public class FileSharingSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileSharingSvcApplication.class, args);
	}

}
