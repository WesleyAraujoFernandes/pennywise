package br.com.knowledge.pennywise.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce.Cluster.Refresh;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.knowledge.pennywise.domain.dto.LoginRequest;
import br.com.knowledge.pennywise.domain.dto.LoginResponse;
import br.com.knowledge.pennywise.domain.dto.RefreshTokenRequest;
import br.com.knowledge.pennywise.domain.user.User;
import br.com.knowledge.pennywise.model.RefreshToken;
import br.com.knowledge.pennywise.repository.RefreshTokenRepository;
import br.com.knowledge.pennywise.repository.UserRepository;
import br.com.knowledge.pennywise.security.JwtService;
import br.com.knowledge.pennywise.service.RefreshTokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final RefreshTokenService refreshTokenService;
        private final RefreshTokenRepository refreshTokenRepository;
        private final UserRepository userRepository;

        public AuthController(AuthenticationManager authenticationManager,
                        JwtService jwtService, UserRepository userRepository, RefreshTokenService refreshTokenService,
                        RefreshTokenRepository refreshTokenRepository) {
                this.authenticationManager = authenticationManager;
                this.jwtService = jwtService;
                this.refreshTokenService = refreshTokenService;
                this.userRepository = userRepository;
                this.refreshTokenRepository = refreshTokenRepository;
        }

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(
                        @Valid @RequestBody LoginRequest request) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.email(),
                                                request.password()));
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
                String accessToken = jwtService.generateToken(userDetails);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

                String role = userDetails.getAuthorities()
                                .stream()
                                .findFirst()
                                .map(a -> a.getAuthority())
                                .orElse("ROLE_USER");

                return ResponseEntity.ok(
                                new LoginResponse(
                                                accessToken,
                                                refreshToken.getToken(),
                                                user.getUsername(),
                                                role));
        }

        @PostMapping("/refresh")
        public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshTokenRequest request) {
                RefreshToken refreshToken = refreshTokenService.validate(request.refreshToken());
                User user = refreshToken.getUser();
                String accessToken = jwtService.generateToken(user);
                return ResponseEntity.ok(
                                new LoginResponse(
                                                accessToken,
                                                refreshToken.getToken(),
                                                user.getUsername(),
                                                user.getRole().name()));
        }

        @PostMapping
        public ResponseEntity<Void> logout(
                        @RequestBody RefreshTokenRequest request) {

                RefreshToken refreshToken = refreshTokenRepository
                                .findByToken(request.refreshToken())
                                .orElse(null);

                if (refreshToken != null) {
                        refreshTokenRepository.delete(refreshToken);
                }

                return ResponseEntity.noContent().build();
        }
}