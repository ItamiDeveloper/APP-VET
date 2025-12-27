package com.vet.spring.app.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidation12345678901234567890}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extrae el tenantId del token JWT
     */
    public Integer extractTenantId(String token) {
        Claims claims = extractAllClaims(token);
        Object tenantId = claims.get("tenantId");
        return tenantId != null ? Integer.parseInt(tenantId.toString()) : null;
    }
    
    /**
     * Extrae el tipo de usuario del token (USUARIO o SUPER_ADMIN)
     */
    public String extractUserType(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("userType");
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * Genera un token JWT para un usuario de tenant con su tenantId
     */
    public String generateTokenWithTenant(UserDetails userDetails, Integer tenantId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tenantId", tenantId);
        claims.put("userType", "USUARIO");
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * Genera un token JWT para un super admin (sin tenantId)
     */
    public String generateTokenForSuperAdmin(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", "SUPER_ADMIN");
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
