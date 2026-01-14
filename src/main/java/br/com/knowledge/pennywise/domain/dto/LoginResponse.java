package br.com.knowledge.pennywise.domain.dto;

public record LoginResponse(String token, String email, String role) {
}
