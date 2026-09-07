package com.rithikaa.notora.controller;

import com.rithikaa.notora.model.User;
import com.rithikaa.notora.service.EmailService;
import com.rithikaa.notora.service.OtpService;
import com.rithikaa.notora.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;

    public RegisterController(UserService userService, OtpService otpService, EmailService emailService) {
        this.userService = userService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    // ---------------- SHOW REGISTER PAGE ----------------
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // ---------------- REGISTER & SEND OTP ----------------
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String fullname,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {

        email = email.trim().toLowerCase();

        // 1️⃣ Check email already exists
        if (userService.emailExists(email)) {
            return "redirect:/register?error=email";
        }

        // 2️⃣ Create pending user (DO NOT SAVE TO DB YET)
        User pendingUser = new User();
        pendingUser.setFullName(fullname);
        pendingUser.setEmail(email);
        pendingUser.setPassword(password);

        // 3️⃣ Store pending user in session
        session.setAttribute("pendingUser", pendingUser);

        // 4️⃣ Generate & send OTP (OtpService → EmailService)
        otpService.generateAndSendOtp(email);

        // 5️⃣ Go to OTP page
        return "redirect:/loginotp";
    }

    // ---------------- SHOW OTP PAGE ----------------
    @GetMapping("/loginotp")
    public String showOtpPage(HttpSession session, Model model) {

        User pendingUser = (User) session.getAttribute("pendingUser");

        if (pendingUser == null) {
            model.addAttribute("error", "Session expired. Please register again.");
            return "register";
        }

        model.addAttribute("email", pendingUser.getEmail());
        return "loginotp";
    }

    // ---------------- VERIFY OTP ----------------
    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String otp,
            HttpSession session,
            Model model
    ) {

        User pendingUser = (User) session.getAttribute("pendingUser");

        if (pendingUser == null) {
            model.addAttribute("error", "Session expired. Please register again.");
            return "register";
        }

        boolean valid = otpService.verifyOtp(pendingUser.getEmail(), otp);

        if (!valid) {
            model.addAttribute("email", pendingUser.getEmail());
            model.addAttribute("error", "Invalid or expired OTP.");
            return "loginotp";
        }

        // ✅ OTP success → save user
        userService.saveUser(pendingUser);
// ✅ SEND WELCOME EMAIL
        emailService.sendAccountCreatedMail(
                pendingUser.getEmail(),
                pendingUser.getFullName()
        );

        // Cleanup
        session.removeAttribute("pendingUser");

        return "redirect:/accountsuccess";
    }

    // ---------------- RESEND OTP ----------------
    @PostMapping("/resend-otp")
    public String resendOtp(HttpSession session, Model model) {

        User pendingUser = (User) session.getAttribute("pendingUser");

        if (pendingUser == null) {
            model.addAttribute("error", "Session expired. Please register again.");
            return "register";
        }

        otpService.generateAndSendOtp(pendingUser.getEmail());
        model.addAttribute("email", pendingUser.getEmail());
        model.addAttribute("message", "OTP resent successfully!");

        return "loginotp";
    }
    // ---------------- ACCOUNT SUCCESS PAGE ----------------
    @GetMapping("/accountsuccess")
    public String accountSuccess() {
        return "accountsuccess"; // accountsuccess.html
    }

}
