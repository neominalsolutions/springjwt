package com.akbank.springjwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  public String secret;
  public int expirationMs;
}
