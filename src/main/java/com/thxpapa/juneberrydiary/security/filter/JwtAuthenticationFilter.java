package com.thxpapa.juneberrydiary.security.filter;

import com.thxpapa.juneberrydiary.domain.auth.RefreshToken;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.user.UserResponseDto;
import com.thxpapa.juneberrydiary.repository.authRepository.RefreshTokenRepository;
import com.thxpapa.juneberrydiary.repository.userRepository.JuneberryUserRepository;
import com.thxpapa.juneberrydiary.security.provider.TokenProvider;
import com.thxpapa.juneberrydiary.security.service.JuneberryUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JuneberryUserRepository juneberryUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request);

            if (token != null && !token.equalsIgnoreCase("null")) {
                if (tokenProvider.validateToken(token)) {
                    String username = tokenProvider.validateAndGetUsername(token);
                    UserDetails userDetails = juneberryUserDetailsService.loadUserByUsername(username);

                    AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(securityContext);
                } else {
                    RefreshToken rtkInfo = refreshTokenRepository.findByAccessToken(token)
                            .orElseThrow(()-> new RuntimeException("access token not found"));
                    String refreshToken = rtkInfo.getRefreshToken();

                    if (!tokenProvider.validateToken(refreshToken)) {
                        String username = rtkInfo.getUsername();

                        JuneberryUser juneberryUser = juneberryUserRepository.findByUsername(username).orElse(null);
                        String newAccessToken = tokenProvider.generateAccessToken(juneberryUser);

                        refreshTokenRepository.save(RefreshToken.builder()
                                .username(username)
                                .refreshToken(refreshToken)
                                .accessToken(newAccessToken)
                                .build());

                        Cookie cookie = new Cookie("access_token", newAccessToken);
                        cookie.setMaxAge(30 * 60 * 1000);
                        cookie.setHttpOnly(true);
                        response.addCookie(cookie);
                    }
                }
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
