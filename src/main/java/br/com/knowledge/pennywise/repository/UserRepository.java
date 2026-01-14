package br.com.knowledge.pennywise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.knowledge.pennywise.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
