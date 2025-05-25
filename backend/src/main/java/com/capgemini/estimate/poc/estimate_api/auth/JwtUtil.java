package com.capgemini.estimate.poc.estimate_api.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private final String secret = "your-secret-key";
  private final long expiration = 3600_000; // 1h

  public String createToken(String username) {
    var key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder()
        .subject(username)
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key)
        .compact();
  }

  public String getUsername(String token) {
    var key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
  }
}
