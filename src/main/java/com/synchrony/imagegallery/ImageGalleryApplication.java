package com.synchrony.imagegallery;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.synchrony.imagegallery.storage.StorageProperties;
import com.synchrony.imagegallery.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ImageGalleryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageGalleryApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> storageService.init();
	}
}
