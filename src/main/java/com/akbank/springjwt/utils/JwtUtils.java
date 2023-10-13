package com.akbank.springjwt.utils;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.akbank.springjwt.config.JwtProperties;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {

  private JwtProperties jwtProp;

  public JwtUtils(JwtProperties jwtProp) {
    super();
    this.jwtProp = jwtProp;

  }

  // jwt üzerinden authenticated olan kullanıcının subject bilgisini jwtden parse
  // ettik.
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  // accessToken validator
  public boolean validateJwtToken(String accessToken) {
    try {
      // jwt bozulmuş formda encoded bir jwt ise parse işleminde hata meydana gelir.
      // eğer parsa işlemi doğru bir şekilde gerçekleşirse farklı jwt'nin farklı
      // exceptionları süreç sonlandırılır.
      Jwts.parserBuilder().setSigningKey(key()).build().parse(accessToken);
      return true;
    } catch (MalformedJwtException e) {
      System.out.println("Invalid Token");
    } catch (ExpiredJwtException e) {
      System.out.println("Token expired");
    } catch (UnsupportedJwtException e) {
      System.out.println("Token unsupported");
    } catch (IllegalArgumentException e) {
      System.out.println("Jwt Claims is empty");
    }

    return false;
  }

  public String generateAccessToken(Authentication authentication) {
    User userPrinciple = (User) authentication.getPrincipal();

    // setExpiration(new Date(new Date().getTime() + jwtProp))
    return Jwts.builder()
        .setSubject(userPrinciple.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(new Date().getTime() + this.jwtProp.getExpirationMs()))
        .signWith(key(), SignatureAlgorithm.HS512).compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtProp.getSecret()));
  }

}
