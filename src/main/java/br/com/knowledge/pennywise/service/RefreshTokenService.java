package br.com.knowledge.pennywise.service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.knowledge.pennywise.domain.user.User;
import br.com.knowledge.pennywise.model.RefreshToken;
import br.com.knowledge.pennywise.repository.RefreshTokenRepository;
import br.com.knowledge.pennywise.repository.UserRepository;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;

    public RefreshTokenService(
            RefreshTokenRepository repository,
            UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void deleteByUserEmail(String email) {
        repository.deleteByUserEmail(email);
    }

    public String createRefreshToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));

        repository.save(token);

        return token.getToken();
    }

}
