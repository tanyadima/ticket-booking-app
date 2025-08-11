package com.booking_app.booking_app.security;

import com.booking_app.booking_app.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.equals("/login") || path.equals("/register") || path.startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                return;
            }
            String token = authHeader.substring(7);
            if (token.isEmpty()) {
                writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token is empty");
                return;
            }
            jwtUtil.validateToken(token);
            String username = jwtUtil.extractUsername(token);
            List<SimpleGrantedAuthority> authorities = jwtUtil.extractRoles(token)
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
        } catch (JwtException ex) {
            writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        } catch (Exception ex) {
            writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private void writeJsonResponse(HttpServletResponse response, int status, String message) throws IOException {
        String jsonResponse = String.format(
                "{\"message\": \"%s\", \"status\": %d}",
                message, status
        );
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}