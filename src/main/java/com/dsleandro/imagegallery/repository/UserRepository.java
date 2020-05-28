package com.dsleandro.imagegallery.repository;

import com.dsleandro.imagegallery.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long>{
   public User findByUsername(String username);
}