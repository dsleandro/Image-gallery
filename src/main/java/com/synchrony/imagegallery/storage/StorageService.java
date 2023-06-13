package com.synchrony.imagegallery.storage;

import org.springframework.core.io.Resource;

import com.synchrony.imagegallery.entity.User;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void store(String filename, byte[] fileBytes);

	Stream<Path> loadAll(User user);

	Path getPath(String filename);

	Resource loadAsResource(String filename);

}
