package com.rithikaa.notora.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ---------------- OTP EMAIL ----------------
    public void sendOtpMail(String to, String otp) {

        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("Email address is null. OTP not sent.");
        }

        String html =
                "<div style='font-family:Segoe UI; padding:20px;'>" +
                        "<h2 style='color:#f472b6;'>Notora - OTP Verification</h2>" +
                        "<p>Your verification code is:</p>" +
                        "<h1 style='letter-spacing:5px;'>" + otp + "</h1>" +
                        "<p>This OTP is valid for <b>2 minutes</b>.</p>" +
                        "<p style='color:#888;'>If you didn't request this, ignore this mail.</p>" +
                        "</div>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Notora - OTP Verification");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    // ---------------- ACCOUNT CREATED EMAIL ----------------
    public void sendAccountCreatedMail(String to, String name) {

        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("Email address is null. Welcome mail not sent.");
        }

        String html =
                "<div style='font-family:Segoe UI,Arial,sans-serif; background:#f9fafb; padding:40px;'>" +

                        "<div style='max-width:600px; margin:auto; background:white; border-radius:16px; " +
                        "box-shadow:0 10px 30px rgba(0,0,0,0.08); overflow:hidden;'>" +

                        "<div style='background:linear-gradient(135deg,#f472b6,#fb7185); padding:30px; text-align:center;'>" +
                        "<h1 style='color:white; margin:0;'>Welcome to Notora 🎉</h1>" +
                        "<p style='color:#ffe4ef; margin-top:8px;'>Your account has been successfully created</p>" +
                        "</div>" +

                        "<div style='padding:30px; color:#374151;'>" +
                        "<p style='font-size:16px;'>Hi <b>" + name + "</b>,</p>" +

                        "<p style='font-size:15px; line-height:1.6;'>" +
                        "We’re excited to have you on <b>Notora</b> — your smart notes sharing platform." +
                        "</p>" +

                        "<p style='font-size:15px; line-height:1.6;'>" +
                        "Your account is now active. You can log in and start creating, sharing, and organizing your notes instantly." +
                        "</p>" +

                        "<div style='text-align:center; margin:35px 0;'>" +
                        "<a href='http://localhost:8080/login' " +
                        "style='background:linear-gradient(135deg,#f472b6,#fb7185); " +
                        "color:white; padding:14px 28px; text-decoration:none; " +
                        "border-radius:30px; font-weight:600; display:inline-block;'>" +
                        "Log in to Notora" +
                        "</a>" +
                        "</div>" +

                        "<p style='font-size:13px; color:#9ca3af;'>" +
                        "If you didn’t create this account, please ignore this email." +
                        "</p>" +
                        "</div>" +

                        "<div style='background:#f9fafb; padding:18px; text-align:center; font-size:12px; color:#9ca3af;'>" +
                        "© 2025 Notora · A Smart Notes Sharing Platform" +
                        "</div>" +

                        "</div>" +
                        "</div>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("🎉 Welcome to Notora — Account Created Successfully");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send account created email", e);
        }
    }
}
