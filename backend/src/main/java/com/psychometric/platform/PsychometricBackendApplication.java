package com.psychometric.platform;

import com.psychometric.platform.features.user.entity.User;
import com.psychometric.platform.features.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@EnableCaching
@SpringBootApplication
@EnableAsync
public class PsychometricBackendApplication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            if (userRepository.count() == 0) {
                System.out.println("--- Seeding Initial Test Users and Admins ---");

                // 1. Super Admin
                User superAdmin = new User();
                superAdmin.setName("Super Admin");
                superAdmin.setEmail("superadmin@psychometric.com");
                superAdmin.setPassword(passwordEncoder.encode("admin123"));
                superAdmin.setEnabled(true);
                Set<String> superAdminRoles = new HashSet<>();
                superAdminRoles.add("ROLE_SUPER_ADMIN");
                superAdminRoles.add("ROLE_ADMIN");
                superAdmin.setRoles(superAdminRoles);
                userRepository.save(superAdmin);

                // 2. Regular Admin
                User regularAdmin = new User();
                regularAdmin.setName("Jane Admin");
                regularAdmin.setEmail("admin@psychometric.com");
                regularAdmin.setPassword(passwordEncoder.encode("admin123"));
                regularAdmin.setEnabled(true);
                Set<String> adminRoles = new HashSet<>();
                adminRoles.add("ROLE_ADMIN");
                regularAdmin.setRoles(adminRoles);
                userRepository.save(regularAdmin);

                // 3. Default Candidate
                User candidate = new User();
                candidate.setName("Alex Candidate");
                candidate.setEmail("candidate@psychometric.com");
                candidate.setPassword(passwordEncoder.encode("candidate123"));
                candidate.setEnabled(true);
                Set<String> candidateRoles = new HashSet<>();
                candidateRoles.add("ROLE_CANDIDATE");
                candidate.setRoles(candidateRoles);
                userRepository.save(candidate);

                System.out.println("--- Seeding Completed Successfully! ---");
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(PsychometricBackendApplication.class, args);
    }
}
