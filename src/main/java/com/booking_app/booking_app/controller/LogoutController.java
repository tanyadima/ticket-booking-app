package com.booking_app.booking_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class LogoutController {

    @GetMapping("/logout-success")
    public String logoutSuccess(Model model) {
        model.addAttribute("message", "You have been logged out successfully.");
        return "logout-success";
    }
}
