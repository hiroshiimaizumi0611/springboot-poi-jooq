package com.capgemini.estimate.poc.estimate_api.presentation;

import com.capgemini.estimate.poc.estimate_api.auth.JwtUtil;
import com.capgemini.estimate.poc.estimate_api.domain.model.LoginRequest;
import com.capgemini.estimate.poc.estimate_api.domain.model.LoginResponse;
import com.capgemini.estimate.poc.estimate_api.domain.model.LogoutRequest;
import com.capgemini.estimate.poc.estimate_api.domain.model.RefreshRequest;
import com.nimbusds.jwt.SignedJWT;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class AuthController {
  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtUtil jwtUtil;

  @Autowired private RedisTemplate<String, String> redisTemplate;

  static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
    // JWT生成
    String accessToken = jwtUtil.createToken(authentication.getName());
    String refreshToken = UUID.randomUUID().toString();

    // Redisにも保存
    redisTemplate.opsForValue().set(refreshToken, authentication.getName(), Duration.ofDays(1));
    return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
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

  private final String clientId = "7n3n3u8mvl07ptvtskoussnm1f";
  private final String redirectUri = "https://estimate-app.com/callback";
  private final String tokenEndpoint =
      "https://estimate-app.auth.ap-northeast-1.amazoncognito.com/oauth2/token";

  @PostMapping("/callback")
  public ResponseEntity<?> handleCognitoCallback(@RequestParam("code") String code) {
    // ① code からトークン取得
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    String requestBody =
        String.format(
            "grant_type=authorization_code&client_id=%s&code=%s&redirect_uri=%s",
            clientId, code, redirectUri);

    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

    var response = restTemplate.postForEntity(tokenEndpoint, entity, Map.class);
    if (!response.getStatusCode().is2xxSuccessful()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to get tokens");
    }
    var tokenResponse = response.getBody();
    String idToken = (String) tokenResponse.get("id_token");

    // ② 署名検証せず、ペイロードのみ取り出し
    Map<String, Object> userInfo = parseIdTokenWithoutVerify(idToken);

    return ResponseEntity.ok(userInfo);
  }

  // 署名検証せずデコードだけする
  public Map<String, Object> parseIdTokenWithoutVerify(String idToken) {
    try {
      SignedJWT jwt = SignedJWT.parse(idToken);
      return jwt.getJWTClaimsSet().getClaims();
    } catch (Exception e) {
      throw new RuntimeException("IDトークンパース失敗", e);
    }
  }
}
