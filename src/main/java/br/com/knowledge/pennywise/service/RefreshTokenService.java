package br.com.knowledge.pennywise.service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.knowledge.pennywise.domain.user.User;
import br.com.knowledge.pennywise.exception.UnauthorizedException;
import br.com.knowledge.pennywise.model.RefreshToken;
import br.com.knowledge.pennywise.repository.RefreshTokenRepository;

@Service
@Transactional
public class RefreshTokenService {

    @Value("${security.jwt.refresh-expiration}")
    private Duration refreshTokenDuration;

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken create(User user, String token) {
        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setToken(token);
        rt.setExpiryDate(Instant.now().plus(refreshTokenDuration));
        return repository.save(rt);
    }

    public RefreshToken createRefreshToken(User user) {

        // opcional: invalidar tokens antigos
        repository.deleteByUser(user);

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(Instant.now().plus(10, ChronoUnit.SECONDS));

        return repository.save(token);
    }

    public RefreshToken validate(String token) {
        RefreshToken rt = repository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Refresh token inválido"));

        if (rt.isRevoked() || rt.getExpiryDate().isBefore(Instant.now())) {
            throw new UnauthorizedException("Refresh token expirado ou revogado");
        }

        return rt;
    }

    public void revoke(String token) {
        repository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            repository.save(rt);
        });
    }

    public void revokeAll(User user) {
        repository.deleteByUser(user);
    }

    public void deleteByUser(User user) {
        repository.deleteByUser(user);
    }
}
