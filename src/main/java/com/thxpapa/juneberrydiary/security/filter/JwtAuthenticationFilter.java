package com.thxpapa.juneberrydiary.security.filter;

import com.thxpapa.juneberrydiary.repository.authRepository.RefreshTokenRepository;
import com.thxpapa.juneberrydiary.security.provider.TokenProvider;
import com.thxpapa.juneberrydiary.security.service.JuneberryUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JuneberryUserDetailsService juneberryUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request);

            if (token != null && !token.equalsIgnoreCase("null") && tokenProvider.validateToken(token)) {
                String username = tokenProvider.validateAndGetUsername(token);
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        juneberryUserDetailsService.loadUserByUsername(username),
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            } else {
                refreshTokenRepository.findByAccessToken(token)
                        .orElseThrow(()-> new RuntimeException("GO"));
            }
        } catch (Exception e) {
            log.debug("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    // extract token info
    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
