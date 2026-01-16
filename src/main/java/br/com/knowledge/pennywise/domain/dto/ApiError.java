package br.com.knowledge.pennywise.domain.dto;

import java.time.Instant;

public record ApiError(
        int status,
        String message,
        Instant timestamp) {
}
