package com.akbank.springjwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.akbank.springjwt.filters.JwtAuthEntryPoint;
import com.akbank.springjwt.filters.JwtAuthenticationFilter;
import com.akbank.springjwt.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private JwtAuthEntryPoint unauthorizedHandler;

  @Bean(name = "passwordEncoder2")
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAuthenticationFilter authenticationJwtTokenFilter() {
    return new JwtAuthenticationFilter();
  }

  // DaoAuthenticationProvider userDetailsService loadUserByName methodu
  // tetiklemek için kullanık.

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    // password encoded güvenli authentication için gerekli ayağa kaldırdık.
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // AntPathRequestMatcher restfulservisler için request path tanımlarında
    // kullanıyoruz
    // api kullandığımızda dolayı STATELESS session seçimi yaptık.

    http
        .cors(cors -> cors.disable())
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/api/home/admin")).hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.GET, "/api/home/manager").hasAnyAuthority("READ", "ROLE_manager")
            .requestMatchers(new AntPathRequestMatcher("/api/home/**")).permitAll()
            .anyRequest().authenticated());
    // yukarıdaki endpoint tanımları dışında kalan tüm endpointler jwt valide
    // olmalıdır.

    // hasAnyAuthority kullanırsak role isimlerinin başına ROLE_ prefix ekleyelim
    // hasAnyRole kullanırsak ROLE_ prefix gerek yok.

    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(),
        UsernamePasswordAuthenticationFilter.class);

    // hasAnyAuthority ile hem role hemde yetki tanımlarını request path bazlı
    // konfigüre ettik.

    return http.build();
  }

}
