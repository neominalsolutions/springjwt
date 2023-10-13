package com.akbank.springjwt.utils;

import java.security.Key;
import java.util.Date;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.akbank.springjwt.config.JwtSettings;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {

  private JwtSettings jwtProp;

  public JwtUtils(JwtSettings jwtProp) {
    super();
    this.jwtProp = jwtProp;

  }

  public String generateAccessToken(Authentication authentication) {
    User userPrinciple = (User) authentication.getPrincipal();

    // setExpiration(new Date(new Date().getTime() + jwtProp))
    return Jwts.builder()
        .setSubject(userPrinciple.getName())
        .setIssuedAt(new Date())
        .setExpiration(new Date(new Date().getTime() + this.jwtProp.getExpirationMs()))
        .signWith(key(), SignatureAlgorithm.HS512).compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtProp.getSecret()));
  }

}
