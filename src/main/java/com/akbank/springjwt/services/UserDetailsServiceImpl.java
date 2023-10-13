package com.akbank.springjwt.services;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Bean(name = "passwordEncoder1")
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // userRepodan bilgileri çektiğimiz kısım

    var passHash = passwordEncoder().encode("password");

    return User.builder().username("user").password(passHash).roles("admin", "manager").authorities("READ").build();

  }

}
