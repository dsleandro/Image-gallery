package com.synchrony.imagegallery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synchrony.imagegallery.entity.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long>{
   public Optional<User> findByUsername(String username);
   public Optional<User> findByUsernameAndPassword(String username, String password);

}