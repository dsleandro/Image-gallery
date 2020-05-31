package com.dsleandro.imagegallery.controller;

import com.dsleandro.imagegallery.entity.User;
import com.dsleandro.imagegallery.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/signup")
    public String formSignup(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") User user) {

        userService.saveUser(user);

        return "redirect:/signin";
    }

}