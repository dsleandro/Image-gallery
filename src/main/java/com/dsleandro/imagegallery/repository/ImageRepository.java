package com.dsleandro.imagegallery.repository;

import java.util.List;

import com.dsleandro.imagegallery.entity.Image;
import com.dsleandro.imagegallery.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("imageRepository")
public interface ImageRepository extends JpaRepository<Image, Long> {
   List<Image> findByUser(User user);
}