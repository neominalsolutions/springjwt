package com.akbank.springjwt.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {

  @GetMapping("")
  public ResponseEntity<?> Index() {
    return ResponseEntity.ok("Everybody sees");
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('admin')")
  public ResponseEntity<?> Admin() {
    return ResponseEntity.ok("Only Admin sees");
  }

  // PreAuthorize ve veya bağlaçları kullanılabilir.
  @GetMapping("/manager")
  @PreAuthorize("hasAuthority('READ') and hasRole('ROLE_manager')")
  public ResponseEntity<?> Manager() {
    return ResponseEntity.ok("Only Manager sees");
  }

}
