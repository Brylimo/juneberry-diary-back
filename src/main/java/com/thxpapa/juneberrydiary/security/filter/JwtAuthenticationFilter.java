package com.thxpapa.juneberrydiary.security.filter;

import com.thxpapa.juneberrydiary.domain.auth.RefreshToken;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.repository.authRepository.RefreshTokenRepository;
import com.thxpapa.juneberrydiary.repository.userRepository.JuneberryUserRepository;
import com.thxpapa.juneberrydiary.security.provider.TokenProvider;
import com.thxpapa.juneberrydiary.security.service.JuneberryUserDetailsService;
import com.thxpapa.juneberrydiary.service.auth.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JuneberryUserDetailsService juneberryUserDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final JuneberryUserRepository juneberryUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseCookieToken(request);

            if (token != null && !token.equalsIgnoreCase("null")) {
                if (tokenProvider.validateToken(token)) {
                    String username = tokenProvider.validateAndGetUsername(token);
                    JuneberryUser juneberryUser = juneberryUserDetailsService.loadUserByUsername(username);

                    AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            juneberryUser,
                            null,
                            juneberryUser.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(securityContext);
                } else {
                    RefreshToken rtkInfo = refreshTokenRepository.findByAccessToken(token)
                            .orElseThrow(()-> new RuntimeException("access token not found"));
                    String refreshToken = rtkInfo.getRefreshToken();

                    if (tokenProvider.validateToken(refreshToken)) {
                        String username = rtkInfo.getUsername();

                        JuneberryUser juneberryUser = juneberryUserRepository.findByUsername(username).orElse(null);
                        String newAccessToken = tokenProvider.generateAccessToken(juneberryUser);
                        refreshTokenService.writeTokenInfo(username, refreshToken, newAccessToken);

                        AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                juneberryUser,
                                null,
                                juneberryUser.getAuthorities()
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                        securityContext.setAuthentication(authenticationToken);
                        SecurityContextHolder.setContext(securityContext);

                        Cookie cookie = new Cookie("access_token", newAccessToken);
                        cookie.setMaxAge(7 * 24 * 60 * 60 * 1000);
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
    private String parseCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            Cookie atkCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("access_token")).findFirst().orElse(null);
            if (atkCookie != null) {
                String token = atkCookie.getValue();
                return token;
            }
        }
        return null;
    }
}
