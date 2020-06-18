package com.dsleandro.imagegallery.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

import com.dsleandro.imagegallery.entity.User;

public interface StorageService {

	void init();

	void store(MultipartFile file);

	Stream<Path> loadAll(User user);

	Path getPath(String filename);

	Resource loadAsResource(String filename);

}
