package com.user_management_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendWelcomeEmail(String to, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Selamat Datang di Marketplace Kami!");
            message.setText("Halo " + name + ",\n\nTerima kasih telah mendaftar di platform kami. ");
            
            mailSender.send(message);
            System.out.println("Welcome email sent to " + to);
        } catch (Exception e) {
            System.err.println("Error sending email to " + to + ": " + e.getMessage());
        }
    }
    @Async
    public void sendOtpEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Kode OTP Reset Password Anda");
            message.setText("Gunakan kode OTP ini untuk mereset password Anda: " + otp +
                            "\nKode ini akan kedaluwarsa dalam 10 menit.");
            mailSender.send(message);
            System.out.println("OTP email sent to " + to);
        } catch (Exception e) {
            System.err.println("Error sending OTP email to " + to + ": " + e.getMessage());
        }
    }
}