package com.capgemini.estimate.poc.estimate_api.presentation;

import com.capgemini.estimate.poc.estimate_api.auth.JwtUtil;
import com.capgemini.estimate.poc.estimate_api.domain.model.LoginRequest;
import com.capgemini.estimate.poc.estimate_api.domain.model.LoginResponse;
import com.capgemini.estimate.poc.estimate_api.domain.model.LogoutRequest;
import com.capgemini.estimate.poc.estimate_api.domain.model.RefreshRequest;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtUtil jwtUtil;

  @Autowired private RedisTemplate<String, String> redisTemplate;

  final static Logger logger = LoggerFactory.getLogger(AuthController.class);

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(), loginRequest.getPassword()));
      // JWT生成
      String accessToken = jwtUtil.createToken(authentication.getName());
      String refreshToken = UUID.randomUUID().toString();

      // Redisにも保存
      redisTemplate.opsForValue().set(refreshToken, authentication.getName(), Duration.ofDays(1));
      return ResponseEntity.ok(new LoginResponse(accessToken,refreshToken));
    } catch (AuthenticationException ex) {
      throw new RuntimeException("Invalid username or password");
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestBody(required = false) LogoutRequest req) {
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String jwt = authHeader.substring(7);
        long ttl = jwtUtil.getRemainingExpiration(jwt);
        redisTemplate.opsForValue().set("BLACKLIST:" + jwt, "true", ttl, TimeUnit.SECONDS);
    }
    
    if (req != null && req.getRefreshToken() != null) {
        redisTemplate.delete(req.getRefreshToken());
    }
    return ResponseEntity.ok().build();
  }

@PostMapping("/refresh")
public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest req) {
    String refreshToken = req.getRefreshToken();
    String username = redisTemplate.opsForValue().get(refreshToken);

    if (username == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String newAccessToken = jwtUtil.createToken(username);
    String newRefreshToken = UUID.randomUUID().toString();

    redisTemplate.delete(refreshToken);
    redisTemplate.opsForValue().set(newRefreshToken, username, Duration.ofDays(1));

    return ResponseEntity.ok(new LoginResponse(newAccessToken, newRefreshToken));
}
}
