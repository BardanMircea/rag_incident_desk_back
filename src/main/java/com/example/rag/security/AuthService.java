package com.example.rag.security;


import com.example.rag.dto.AuthResponse;
import com.example.rag.dto.LoginRequest;
import com.example.rag.dto.RegisterRequest;
import com.example.rag.model.AppUser;
import com.example.rag.model.Role;
import com.example.rag.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role role = request.role() == null
                ? Role.USER
                : Role.valueOf(request.role().toUpperCase());

        AppUser user = AppUser.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();

        AppUser saved = repository.save(user);
        String token = jwtService.generateToken(saved);

        return new AuthResponse(token, saved.getEmail(), saved.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        AppUser user = repository.findByEmail(request.email())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }
}
