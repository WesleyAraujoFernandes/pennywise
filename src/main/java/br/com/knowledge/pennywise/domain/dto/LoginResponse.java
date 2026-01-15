package br.com.knowledge.pennywise.domain.dto;

public record LoginResponse(String accessToken, String refreshToken, String email, String role) {
}
