package com.capgemini.estimate.poc.estimate_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfig {
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user =
        User.withUsername("testuser").password("{noop}password").roles("USER").build();
    return new InMemoryUserDetailsManager(user);
  }
}
