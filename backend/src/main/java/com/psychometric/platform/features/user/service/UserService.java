package com.psychometric.platform.features.user.service;


import com.psychometric.platform.common.util.HtmlSanitizer;
import com.psychometric.platform.features.user.dto.request.AdminCreateRequest;
import com.psychometric.platform.features.user.dto.response.AdminResponse;
import com.psychometric.platform.features.auth.dto.request.SignupRequest;
import com.psychometric.platform.features.user.dto.response.CandidateResponse;
import com.psychometric.platform.features.user.dto.request.CandidateUpdateRequest;
import com.psychometric.platform.features.user.dto.request.CandidateCreateRequest;
import com.psychometric.platform.common.exception.DuplicateResourceException;
import com.psychometric.platform.features.user.entity.User;
import com.psychometric.platform.features.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private com.psychometric.platform.features.assessment.repository.AssessmentAttemptRepository attemptRepository;

    @org.springframework.transaction.annotation.Transactional
    public void deleteUserPermanently(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (attemptRepository != null) {
            List<com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt> attempts = attemptRepository.findByCandidateIdOrderByCreatedAtDesc(id);
            if (attempts != null && !attempts.isEmpty()) {
                attemptRepository.deleteAll(attempts);
            }
        }
        userRepository.delete(user);
    }

    public List<AdminResponse> getAllAdmins()
    {
        return userRepository.findAll().stream()
                // Filter to only include users with admin roles
                .filter(user -> user.getRoles().contains("ROLE_ADMIN") || user.getRoles().contains("ROLE_SUPER_ADMIN"))
                .map(user -> new AdminResponse(user.getId(), user.getName(), user.getEmail(), user.getRoles(), user.isEnabled()))
                .collect(Collectors.toList());
    }

    public AdminResponse createAdminUser(AdminCreateRequest request)
    {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(HtmlSanitizer.sanitize(request.getName()));
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(request.getRoles());
        user.setEnabled(false); // Active immediately

        User savedUser = userRepository.save(user);
        return new AdminResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRoles(), savedUser.isEnabled());
    }


    public AdminResponse updateAdminUser(int id , AdminCreateRequest request)
    {

        User user  = userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("Admin account not found."));
        user.setName(HtmlSanitizer.sanitize(request.getName()));
        user.setRoles(request.getRoles());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        User updatedUser = userRepository.save(user);
        return new AdminResponse(updatedUser.getId(), updatedUser.getName(),
                updatedUser.getEmail(), updatedUser.getRoles(), updatedUser.isEnabled());
    }


    public void deactivateAdminUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin account not found."));
        user.setEnabled(false);
        userRepository.save(user);
    }



    public User createUser(SignupRequest request) {
        User user = new User();

        user.setName(HtmlSanitizer.sanitize(request.getName()));
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        Set<String> roles = new java.util.HashSet<>();
        roles.add("ROLE_CANDIDATE");

        user.setRoles(roles);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    
    public CandidateResponse createCandidateUser(CandidateCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists.");
        }

        User user = new User();
        user.setName(HtmlSanitizer.sanitize(request.getName()));
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<String> roles = new java.util.HashSet<>();
        roles.add("ROLE_CANDIDATE");
        user.setRoles(roles);
        user.setEnabled(true); // Default is enabled = true when created from Admin Panel

        User savedUser = userRepository.save(user);

        return new CandidateResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.isEnabled()
        );
    }

    public List<CandidateResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().contains("ROLE_CANDIDATE"))
                .map(user -> new CandidateResponse(
                         user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.isEnabled()))
                .toList();
    }

    public CandidateResponse updateUser(Long id, CandidateUpdateRequest request) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(HtmlSanitizer.sanitize(request.getName()));
        existingUser.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User savedUser = userRepository.save(existingUser);

        return new CandidateResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.isEnabled()
        );
    }

    public void deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(false);

        userRepository.save(user);
    }


    public void reactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(true);

        userRepository.save(user);
    }

}
