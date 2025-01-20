package com.booking_app.booking_app.configuration;

import com.booking_app.booking_app.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers("/login", "/register", "/logout-success").permitAll()  // Allow login and registration without authentication
                .requestMatchers(HttpMethod.POST, "/movies", "/showtimes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/book").hasRole("CUSTOMER")
                .anyRequest().authenticated()  // All other requests require authentication
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())// Return 403 if authentication fails
                .and()
                .formLogin()
                .loginPage("/login")  // Custom login page URL
                .loginProcessingUrl("/perform_login")  // URL to process login
                .defaultSuccessUrl("/home", true)  // Redirect to /home on successful login
                .failureUrl("/login?error=true")  // Redirect to login with error query parameter
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // Define the logout URL
                .logoutSuccessUrl("/logout-success") // Redirect after logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // Allow GET
                .invalidateHttpSession(true) // Invalidate session
                .clearAuthentication(true) // Clear authentication
                .permitAll();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Using BCrypt for password encoding
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}