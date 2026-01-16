package br.com.knowledge.pennywise.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.knowledge.pennywise.domain.user.User;
import br.com.knowledge.pennywise.model.RefreshToken;
import br.com.knowledge.pennywise.repository.RefreshTokenRepository;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken createRefreshToken(User user) {

        // opcional: invalidar tokens antigos
        repository.deleteByUser(user);

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));

        return repository.save(token);
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

    public void deleteByUser(User user) {
        repository.deleteByUser(user);
    }
}
