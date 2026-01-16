package br.com.knowledge.pennywise.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.knowledge.pennywise.domain.dto.LoginRequest;
import br.com.knowledge.pennywise.domain.dto.LoginResponse;
import br.com.knowledge.pennywise.domain.dto.TokenResponse;
import br.com.knowledge.pennywise.domain.user.User;
import br.com.knowledge.pennywise.model.RefreshToken;
import br.com.knowledge.pennywise.repository.RefreshTokenRepository;
import br.com.knowledge.pennywise.repository.UserRepository;
import br.com.knowledge.pennywise.security.JwtService;
import br.com.knowledge.pennywise.service.RefreshTokenService;
import br.com.knowledge.pennywise.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final RefreshTokenService refreshTokenService;
        private final UserRepository userRepository;

        public AuthController(AuthenticationManager authenticationManager,
                        JwtService jwtService, UserRepository userRepository, RefreshTokenService refreshTokenService,
                        RefreshTokenRepository refreshTokenRepository) {
                this.authenticationManager = authenticationManager;
                this.jwtService = jwtService;
                this.refreshTokenService = refreshTokenService;
                this.userRepository = userRepository;
        }

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(
                        @Valid @RequestBody LoginRequest request) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.email(),
                                                request.password()));

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                User user = userRepository.findByEmail(userDetails.getUsername())
                                .orElseThrow();

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
        public TokenResponse refresh(HttpServletRequest request) {

                String refreshToken = CookieUtils.getRefreshToken(request);

                RefreshToken rt = refreshTokenService.validate(refreshToken);

                String newAccessToken = jwtService.generateAccessToken(rt.getUser());

                return new TokenResponse(newAccessToken);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(HttpServletRequest request) {

                String refreshToken = CookieUtils.getRefreshToken(request);

                if (refreshToken != null) {
                        refreshTokenService.revoke(refreshToken);
                }

                ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/auth/refresh")
                                .maxAge(0)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .build();
        }

        /*
         * @PostMapping("/logout")
         * public ResponseEntity<Void> logout(
         * 
         * @RequestBody RefreshTokenRequest request) {
         * 
         * RefreshToken refreshToken = refreshTokenRepository
         * .findByToken(request.refreshToken())
         * .orElse(null);
         * 
         * if (refreshToken != null) {
         * refreshTokenRepository.delete(refreshToken);
         * }
         * 
         * return ResponseEntity.noContent().build();
         * }
         */
}