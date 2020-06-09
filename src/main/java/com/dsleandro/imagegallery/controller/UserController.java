package com.dsleandro.imagegallery.controller;

import com.dsleandro.imagegallery.entity.User;
import com.dsleandro.imagegallery.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/signin")
    public String formSignin() {
        return "signin";
    }

    @PostMapping("/signin")
    public String signin(@RequestBody String username, @RequestBody String password, Model model) {

        User user = userService.getUser(username, password);

        if (user != null) {
            model.addAttribute("loggedUser", user);
            return "redirect:/";
        }
        model.addAttribute("loginError", "Invalid username or password");
        return "redirect:/signin";

    }

    @GetMapping("/signup")
    public String formSignup(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.saveUser(user);

        return "redirect:/signin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {

        boolean deleted = userService.deleteUser(id);
        if (deleted)
            return "redirect:/signin";
        else
            return "redirect:/delete";

    }

    @PutMapping("/update/{oldUserId}")
    public String updateUser(@PathVariable long oldUserId, @ModelAttribute("user") User newUser, Model model) {

        User user = userService.findUser(oldUserId);

        if (user != null) {
            user.setUsername(newUser.getUsername());
            user.setPassword(newUser.getPassword());

            userService.updateUser(user);

            model.addAttribute("updatedUser", user);
        }

        return null;
    }

}