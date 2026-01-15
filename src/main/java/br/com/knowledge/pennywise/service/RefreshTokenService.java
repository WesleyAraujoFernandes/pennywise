package br.com.knowledge.pennywise.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.knowledge.pennywise.domain.user.User;
import br.com.knowledge.pennywise.model.RefreshToken;
import br.com.knowledge.pennywise.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);
    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken createRefreshToken(User user) {
        repository.deleteByUser(user); // garante apenas 1 ativo

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_DURATION));

        return repository.save(refreshToken);
    }

    public RefreshToken validate(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(refreshToken);
            throw new RuntimeException("Refresh token expirado");
        }

        return refreshToken;
    }

    public void delete(RefreshToken token) {
        repository.delete(token);
    }

}
