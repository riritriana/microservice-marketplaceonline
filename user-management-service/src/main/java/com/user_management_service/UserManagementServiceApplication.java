package com.user_management_service;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.user_management_service.service.AuthService;

@SpringBootApplication
@EnableAsync 
public class UserManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementServiceApplication.class, args);
	}

	/**
     * Bean ini akan dijalankan secara otomatis SETELAH aplikasi sepenuhnya siap.
     * Ini adalah cara yang aman untuk menjalankan logika inisialisasi data.
     * @param authService Service yang berisi method untuk membuat admin.
     * @return ApplicationRunner instance.
     */
    @Bean
    public ApplicationRunner applicationRunner(AuthService authService) {
        return args -> {
            authService.initializeAdmin();
        };
    }
}
