package com.user_management_service.service;


import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user_management_service.dto.AuthResponse;
import com.user_management_service.dto.LoginRequest;
import com.user_management_service.dto.RegisterRequest;
import com.user_management_service.dto.ResetPasswordRequest;
import com.user_management_service.model.Role;
import com.user_management_service.model.User;
import com.user_management_service.repository.UserRepository;
import com.user_management_service.security.JwtUtil;

import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

   
    public void initializeAdmin() {
        // Cek jika admin belum ada di database
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User admin = User.builder()
                    .name("Default Admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin12345"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println(">>> Default admin user created successfully!");
        }
    }

  public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STAFF)
                .build();
        userRepository.save(user);
        emailService.sendWelcomeEmail(user.getEmail(), user.getName());
        
        // Buat claims untuk token
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        var jwtToken = jwtUtil.generateToken(extraClaims, user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        
        // Buat claims untuk token
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        
        var jwtToken = jwtUtil.generateToken(extraClaims, user);
        return new AuthResponse(jwtToken);
    }    
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        user.setOtp(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10)); // Kedaluwarsa dalam 10 menit
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + request.getEmail()));

        // Validasi OTP
        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP.");
        }

        // Validasi waktu kedaluwarsa OTP
        if (user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired.");
        }

        // Jika semua valid, update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // Hapus OTP setelah digunakan
        user.setOtp(null);
        user.setOtpExpiryTime(null);
        
        userRepository.save(user);
    }
}

