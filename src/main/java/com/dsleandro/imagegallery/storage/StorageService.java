package com.dsleandro.imagegallery.storage;

import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.stream.Stream;

import com.dsleandro.imagegallery.entity.User;

public interface StorageService {

	void init();

	void store(String filename, byte[] fileBytes);

	Stream<Path> loadAll(User user);

	Path getPath(String filename);

	Resource loadAsResource(String filename);

}
