package com.booking_app.booking_app.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        return "login";  // Return the login.html page
    }

    @GetMapping("/home")
    public String homePage(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        String cookieValue = "No cookies found";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    cookieValue = "JSESSIONID=" + cookie.getValue();
                    break;
                }
            }
        }

        model.addAttribute("cookieValue", cookieValue);
        return "home";  // Redirect to a home page after successful login
    }

    @PostMapping("/perform_login")
    public String performLogin(@RequestParam String username, @RequestParam String password) {
        // You can handle custom login logic here, but Spring Security does this automatically
        return "redirect:/home";  // Redirect after successful login
    }
}
