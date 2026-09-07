
package com.rithikaa.notora.controller;

import com.rithikaa.notora.model.User;
import com.rithikaa.notora.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // ---------------- SHOW LOGIN PAGE ----------------
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // login.html
    }

    // ---------------- HANDLE LOGIN ----------------
    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {

        email = email.trim().toLowerCase();

        // 1️⃣ Check if user exists
        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }

        // 2️⃣ Check password (plain for now)
        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }

        // 3️⃣ Login success → store session
        session.setAttribute("loggedInUser", user);

        return "redirect:/dashboard";
    }

    // ---------------- LOGOUT ----------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
