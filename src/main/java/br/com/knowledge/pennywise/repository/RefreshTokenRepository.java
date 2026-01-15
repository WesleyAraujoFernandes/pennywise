package br.com.knowledge.pennywise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.knowledge.pennywise.domain.user.User;
import br.com.knowledge.pennywise.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
