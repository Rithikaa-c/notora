package com.rithikaa.notora.controller;

import com.rithikaa.notora.model.Admin;
import com.rithikaa.notora.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminLoginController {

    private final AdminService adminService;

    public AdminLoginController(AdminService adminService) {
        this.adminService = adminService;
    }

    // SHOW ADMIN LOGIN
    @GetMapping("/login")
    public String showLogin() {
        return "admin-login";
    }

    // HANDLE ADMIN LOGIN
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        email = email.trim().toLowerCase();

        Admin admin = adminService.findByEmail(email);

        if (admin == null || !admin.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid admin credentials");
            return "admin-login";
        }

        session.setAttribute("admin", admin);
        return "redirect:/admin/dashboard";
    }

    // ADMIN DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin-dashboard";
    }
}
