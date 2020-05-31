package com.dsleandro.imagegallery.service;

import com.dsleandro.imagegallery.entity.User;
import com.dsleandro.imagegallery.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public boolean saveUser(User user) {

        if (existUser(user.getId())) {
            return false;
        }

        userRepository.save(user);
        return false;

    }

    public User getUser(String username, String password){

        if (username != null && password != null){
             return userRepository.findByUsernameAndPassword(username, password).orElseGet(null);
        }

        return null;
    }

    public boolean deleteUser(long userId) {

        if (existUser(userId)) {
            userRepository.deleteById(userId);
            return true;
        }

        return false;
    }

    public boolean updateUser(User user) {
        
        if (existUser(user.getId())) {
            userRepository.save(user);
            return true;
        }

        return false;

    }

    public User findUser(long userId) {

        return userRepository.findById(userId).orElseGet(null);

    }

    private boolean existUser(long userId) {

        boolean existentUser = userRepository.findById(userId).isPresent();

        if (existentUser)
            return true;
        else
            return false;

    }

}