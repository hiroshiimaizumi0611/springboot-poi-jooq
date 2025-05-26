package com.capgemini.estimate.poc.estimate_api.presentation;

import com.capgemini.estimate.poc.estimate_api.auth.JwtUtil;
import com.capgemini.estimate.poc.estimate_api.domain.model.LoginRequest;
import com.capgemini.estimate.poc.estimate_api.domain.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(), loginRequest.getPassword()));
      // JWT生成
      String token = jwtUtil.createToken(authentication.getName());
      // Redisにも保存
      redisTemplate.opsForValue().set(token, "valid");
      return new LoginResponse(token);
    } catch (AuthenticationException ex) {
      throw new RuntimeException("Invalid username or password");
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    // JWTに対応するRedisのキー名で消す（実装に合わせて）
    redisTemplate.delete(token);
    return ResponseEntity.ok().build();
  }
}
