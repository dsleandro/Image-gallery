package com.dsleandro.imagegallery.service;

import java.util.Collections;

import com.dsleandro.imagegallery.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.dsleandro.imagegallery.entity.User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Invalid username"));

        return new User(username, user.getPassword(), Collections.emptyList());

    }

}