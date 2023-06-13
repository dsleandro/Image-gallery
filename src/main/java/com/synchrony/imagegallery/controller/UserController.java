package com.synchrony.imagegallery.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import com.synchrony.imagegallery.entity.User;
import com.synchrony.imagegallery.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/signin")
    public String formSignin(Model model, Principal user) {

        // logout user if it is logged
        if (user != null) {
            return "redirect:/logout";
        }

        model.addAttribute("user", new User());
        return "signin";

    }

    @GetMapping("/signup")
    public String formSignup(Model model, Principal user) {

        if (user != null) {
            return "redirect:/logout";
        }

        model.addAttribute("user", new User());
        return "signup";

    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // if username exist, return to signup
        if (userService.existUser(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "username already exists");
            return "signup";
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.saveUser(user);

        return "redirect:/signin";
    }
}