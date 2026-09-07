package com.rithikaa.notora.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final EmailService emailService;

    public OtpService(EmailService emailService) {
        this.emailService = emailService;
    }

    // Holds OTP + creation time
    private static class OtpData {
        private final String otp;
        private final LocalDateTime createdAt;

        OtpData(String otp, LocalDateTime createdAt) {
            this.otp = otp;
            this.createdAt = createdAt;
        }
    }

    // email → otp data (thread-safe)
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    private static final long OTP_VALIDITY_SECONDS = 120; // 2 minutes

    // ---------------- GENERATE & SEND OTP ----------------
    public void generateAndSendOtp(String email) {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }

        String otp = String.format("%06d", new Random().nextInt(1_000_000));

        otpStore.put(email, new OtpData(otp, LocalDateTime.now()));

        emailService.sendOtpMail(email, otp);
    }

    // ---------------- VERIFY OTP ----------------
    public boolean verifyOtp(String email, String enteredOtp) {

        if (email == null || enteredOtp == null) {
            return false;
        }

        OtpData data = otpStore.get(email);

        if (data == null) {
            return false;
        }

        // OTP match
        if (!data.otp.equals(enteredOtp)) {
            return false;
        }

        // Expiry check
        long secondsPassed =
                Duration.between(data.createdAt, LocalDateTime.now()).getSeconds();

        if (secondsPassed > OTP_VALIDITY_SECONDS) {
            otpStore.remove(email);
            return false;
        }

        // One-time use
        otpStore.remove(email);
        return true;
    }
}
