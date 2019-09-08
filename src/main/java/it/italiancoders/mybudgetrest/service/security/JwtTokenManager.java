package it.italiancoders.mybudgetrest.service.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface JwtTokenManager {
    Optional<UserDetails> getUserDetails(String authToken) throws ExpiredJwtException;

    void validateAccessToken(String authToken, UserDetails userDetails) throws Exception;

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateAccessTokenByRefreshToken(String refreshToken) throws Exception;
}
