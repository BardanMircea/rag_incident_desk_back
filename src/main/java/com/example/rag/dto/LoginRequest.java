package com.example.rag.dto;

public record LoginRequest(
        String email,
        String password
) {}