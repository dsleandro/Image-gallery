package com.dsleandro.imagegallery.repository;

import com.dsleandro.imagegallery.entity.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("imageRepository")
public interface ImageRepository extends JpaRepository<Image, Long> {
    
}