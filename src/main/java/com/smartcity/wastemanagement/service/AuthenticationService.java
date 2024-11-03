package com.smartcity.wastemanagement.service;

import com.smartcity.wastemanagement.dto.AuthDTO;
import com.smartcity.wastemanagement.model.Role;
import com.smartcity.wastemanagement.model.User;
import com.smartcity.wastemanagement.repository.UserRepository;
import com.smartcity.wastemanagement.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthDTO.AuthResponse register(AuthDTO.RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        
        AuthDTO.AuthResponse response = new AuthDTO.AuthResponse();
        response.setToken(jwt);
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        
        return response;
    }

    public AuthDTO.AuthResponse authenticate(AuthDTO.LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String jwt = jwtService.generateToken(user);
        
        AuthDTO.AuthResponse response = new AuthDTO.AuthResponse();
        response.setToken(jwt);
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        
        return response;
    }
}