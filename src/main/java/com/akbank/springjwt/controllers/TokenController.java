package com.akbank.springjwt.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akbank.springjwt.dtos.TokenRequestDto;
import com.akbank.springjwt.dtos.TokenResponseDto;
import com.akbank.springjwt.utils.JwtUtils;

@RestController
@RequestMapping("api/auth")
public class TokenController {

  private JwtUtils jwtUtils;
  private AuthenticationManager authenticationManager;

  public TokenController(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
    super();
    this.jwtUtils = jwtUtils;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("token")
  public ResponseEntity<TokenResponseDto> generateToken(@RequestBody TokenRequestDto request) {

    // Spirng Security ile request gelen değerlere göre authentication olup
    // olmadığını kontrol edicez.
    var authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    var token = this.jwtUtils.generateAccessToken(authentication);

    var response = new TokenResponseDto();
    response.setAccessToken(token);

    return ResponseEntity.ok(response);
  }

}
