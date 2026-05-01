package com.sms.studentmanagementsystem.controller;

import com.sms.studentmanagementsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {

        if (userService.usernameExists(username)) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        userService.registerUser(username, password);
        return "redirect:/login?registered";
    }
}