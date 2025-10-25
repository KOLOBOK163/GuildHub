package com.guildhub.Config;

import com.guildhub.Security.JwtUtils;
import com.guildhub.Security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final static String AUTHORIZATION = "Authorization";

    private final JwtUtils jwtUtils;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService)
    {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        String jwt = null;
        String username = null;

        if(Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer "))
        {
            jwt = authorizationHeader.substring(7);
            username = jwtUtils.getUsernameFromJwtToken(jwt);
        }

        if(Objects.nonNull(username) && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean isTokenValidated = jwtUtils.validateToken(jwt, userDetails);
            if(isTokenValidated){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
