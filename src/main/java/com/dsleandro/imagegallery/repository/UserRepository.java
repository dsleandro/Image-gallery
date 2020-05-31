package com.dsleandro.imagegallery.repository;

import java.util.Optional;

import com.dsleandro.imagegallery.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long>{
   public Optional<User> findByUsername(String username);
   public Optional<User> findByUsernameAndPassword(String username, String password);

}