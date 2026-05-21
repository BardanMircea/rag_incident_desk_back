package com.example.rag.dto;

public record RegisterRequest(
        String email,
        String password,
        String role
) {}