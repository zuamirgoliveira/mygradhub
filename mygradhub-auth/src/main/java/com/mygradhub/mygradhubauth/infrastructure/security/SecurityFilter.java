package com.mygradhub.mygradhubauth.infrastructure.security;

import com.mygradhub.mygradhubauth.domain.exception.AuthenticationProcessingException;
import com.mygradhub.mygradhubauth.domain.service.AuthServiceImpl;
import com.mygradhub.mygradhubauth.shared.AppConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthServiceImpl authService;

    public SecurityFilter(TokenService tokenService, AuthServiceImpl authService) {
        this.tokenService = tokenService;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = this.recoverToken(request);
            if (token != null && tokenService.isValid(token)) {
                String login = tokenService.validateToken(token);
                UserDetails userDetails = authService.loadUserByUsername(login);
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new AuthenticationProcessingException(AppConstants.ERROR_PROCESSING_AUTHENTICATION_FILTER, e);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader(AppConstants.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(AppConstants.BEARER_)) {
            return authHeader.replace(AppConstants.BEARER_, "");
        }
        return null;
    }
}
