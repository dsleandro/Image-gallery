package com.dsleandro.imagegallery.controller;

import java.security.Principal;

import javax.validation.Valid;

import com.dsleandro.imagegallery.entity.User;
import com.dsleandro.imagegallery.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
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
        } else {
            model.addAttribute("user", new User());
            return "signin";
        }
    }

    @PostMapping("/signin")
    public String signin(@Valid @ModelAttribute("user") User user, Model model, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/signin";
        }

        User loggedUser = userService.getUser(user.getUsername(), user.getPassword());

        if (loggedUser != null) {
            return "redirect:/";
        }

        model.addAttribute("loginError", "Invalid username or password");
        return "redirect:/signin";

    }

    @GetMapping("/signup")
    public String formSignup(Model model, Principal user) {

        if (user != null) {
            return "redirect:/logout";
        } else {
            model.addAttribute("user", new User());
            return "signup";
        }
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/signup";
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.saveUser(user);

        return "redirect:/signin";
    }
}