package com.synchrony.imagegallery.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.synchrony.imagegallery.entity.Image;
import com.synchrony.imagegallery.entity.User;
import com.synchrony.imagegallery.repository.ImageRepository;

@Service
public class FileSystemStorageService implements StorageService {

	@Autowired
	private ImageRepository imageRepository;

	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void store(String filename, byte[] fileBytes) {
		try {

			//Check if is not an empty file
			if (!(fileBytes != null  && fileBytes.length > 0)) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory " + filename);
			}

			Files.write(getPath(filename), fileBytes, StandardOpenOption.CREATE);

		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	@Override
	public Stream<Path> loadAll(User user) {
		try {
			List<Image> images = imageRepository.findByUser(user);

			//returns stream of image paths that match with logged user
			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation)).filter(path -> images.stream().anyMatch(img -> img.getPath().equals(path.toString())))
					.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path getPath(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = getPath(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);
			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void init() {
		try {
			if (Files.notExists(rootLocation))
				Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
