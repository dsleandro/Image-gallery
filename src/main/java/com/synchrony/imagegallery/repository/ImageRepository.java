package com.synchrony.imagegallery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synchrony.imagegallery.entity.Image;
import com.synchrony.imagegallery.entity.User;

@Repository("imageRepository")
public interface ImageRepository extends JpaRepository<Image, Long> {
   List<Image> findByUser(User user);
}