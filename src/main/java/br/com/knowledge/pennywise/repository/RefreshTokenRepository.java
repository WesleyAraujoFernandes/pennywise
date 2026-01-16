package br.com.knowledge.pennywise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.knowledge.pennywise.domain.user.User;
import br.com.knowledge.pennywise.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("delete from RefreshToken rt where rt.user.email = :email")
    void deleteByUserEmail(@Param("email") String email);

    void deleteByUser(User user);

    void deleteAllByUserEmail(@Param("email") String email);
}
