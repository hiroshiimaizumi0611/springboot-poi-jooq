package com.capgemini.estimate.poc.estimate_api.presentation;

import com.capgemini.estimate.poc.estimate_api.auth.JwtUtil;
import com.capgemini.estimate.poc.estimate_api.domain.model.LoginRequest;
import com.capgemini.estimate.poc.estimate_api.domain.model.LoginResponse;
import com.capgemini.estimate.poc.estimate_api.domain.model.LogoutRequest;
import com.capgemini.estimate.poc.estimate_api.domain.model.RefreshRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class AuthController {
  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtUtil jwtUtil;

  @Autowired private RedisTemplate<String, String> redisTemplate;

  @Autowired private RestTemplate restTemplate;

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

  private final String clientId = "j13taan8rcthtpojjsq30c62k";
  private final String clientSecret = "1hp1gn3ek7rv36o0q4bae2comtoro8eseaf0pkn7uof7eq0ga801";
  private final String redirectUri = "https://estimate-app.com/callback";
  private final String tokenEndpoint =
      "https://estimate-app.auth.ap-northeast-1.amazoncognito.com/oauth2/token";

  @PostMapping("/callback")
  public ResponseEntity<Tokens> callback(@RequestBody Map<String, String> body) {
    String code = body.get("code");

    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("grant_type", "authorization_code");
    form.add("code", code);
    form.add("redirect_uri", redirectUri);
    form.add("client_id", clientId);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBasicAuth(clientId, clientSecret, StandardCharsets.UTF_8);

    Tokens t = postForTokens(form, headers);

    return ResponseEntity.ok(t);
  }

  // ---- リフレッシュ --------------------------------------------
  // @PostMapping("/refresh")
  // public ResponseEntity<Tokens> refresh(@RequestBody Map<String,String> body){
  //             MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
  //     form.add("grant_type", "refresh_token");
  //     form.add("refresh_token", body.get("refreshToken"));
  //     form.add("client_id", clientId);

  //     HttpHeaders headers = buildHeadersWithBasicAuth();
  //     Tokens tokens = postForTokens(form, headers);
  //     return ResponseEntity.ok(tokens);
  // }

  private Tokens postForTokens(MultiValueMap<String, String> form, HttpHeaders h) {
    HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(form, h);
    return Objects.requireNonNull(restTemplate.postForObject(tokenEndpoint, req, Tokens.class));
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Tokens {

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private long expiresIn;

    public String getIdToken() {
      return idToken;
    }

    public String getAccessToken() {
      return accessToken;
    }

    public String getRefreshToken() {
      return refreshToken;
    }

    public long getExpiresIn() {
      return expiresIn;
    }

    public String getSub() {
      try {
        String payload = idToken.split("\\.")[1];
        String json = new String(Base64.getUrlDecoder().decode(payload));
        return new ObjectMapper().readTree(json).get("sub").asText();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
  }

  @Configuration
  static class RestConfig {

    @Bean
    public RestTemplate restTemplate() {
      RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
      return restTemplateBuilder.build();
    }
  }
}
