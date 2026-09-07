package com.rithikaa.notora.controller;

import com.rithikaa.notora.model.User;
import com.rithikaa.notora.service.OtpService;
import com.rithikaa.notora.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ForgotPasswordController {

    private final UserService userService;
    private final OtpService otpService;

    public ForgotPasswordController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    // STEP 1 → SHOW PAGE
    @GetMapping("/forget-otp")
    public String showPage(Model model) {
        model.addAttribute("step", 1);
        return "forget-otp";
    }

    // STEP 2 → SEND OTP
    @PostMapping("/send-forgot-otp")
    public String sendOtp(@RequestParam String email,
                          HttpSession session,
                          Model model) {

        User user = userService.findByEmail(email.toLowerCase());

        if (user == null) {
            model.addAttribute("error", "Email not registered.");
            model.addAttribute("step", 1);
            return "forget-otp";
        }

        otpService.generateAndSendOtp(email);
        session.setAttribute("resetEmail", email);

        model.addAttribute("step", 2);
        return "forget-otp";
    }

    // STEP 3 → VERIFY OTP
    @PostMapping("/verify-forgot-otp")
    public String verifyOtp(@RequestParam String otp,
                            HttpSession session,
                            Model model) {

        String email = (String) session.getAttribute("resetEmail");

        if (email == null || !otpService.verifyOtp(email, otp)) {
            model.addAttribute("error", "Invalid or expired OTP.");
            model.addAttribute("step", 2);
            return "forget-otp";
        }

        model.addAttribute("step", 3);
        return "forget-otp";
    }

    // STEP 4 → RESET PASSWORD
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String newPassword,
                                HttpSession session,
                                Model model) {

        String email = (String) session.getAttribute("resetEmail");
        User user = userService.findByEmail(email);

        if (user.getPassword().equals(newPassword)) {
            model.addAttribute("error", "Old password cannot be the new password. Try login.");
            model.addAttribute("step", 3);
            return "forget-otp";
        }

        userService.updatePassword(user, newPassword);
        session.invalidate();

        return "redirect:/login";
    }
}
