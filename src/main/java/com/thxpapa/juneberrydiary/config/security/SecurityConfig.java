package com.thxpapa.juneberrydiary.config.security;

import com.thxpapa.juneberrydiary.security.filter.JwtAuthenticationFilter;
import com.thxpapa.juneberrydiary.security.provider.JuneberryAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {
    @Value("${app.front.url}")
    private String frontUrl;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic->basic.disable())
                .sessionManagement(configurer->configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorizeRequests->
                        authorizeRequests
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"),
                                        new AntPathRequestMatcher("/h2-console/**"),
                                        new AntPathRequestMatcher("/actuator/**"),
                                        new AntPathRequestMatcher("/email/**"),
                                        new AntPathRequestMatcher("/token/login"),
                                        new AntPathRequestMatcher("/v1/user"),
                                        new AntPathRequestMatcher("/v1/user/email"),
                                        new AntPathRequestMatcher("/v1/user/username"),
                                        new AntPathRequestMatcher("/v1/user/verification-code"),
                                        new AntPathRequestMatcher("/v1/cal/tags"),
                                        new AntPathRequestMatcher("/v1/blog"),
                                        new AntPathRequestMatcher("/v1/blog/{blogId}/category/categories"),
                                        new AntPathRequestMatcher("/v1/blog/{blogId}/tag/tags"),
                                        new AntPathRequestMatcher("/v1/post/posts"),
                                        new AntPathRequestMatcher("/v1/posts")).permitAll() // 공개 경로
                                .requestMatchers(new AntPathRequestMatcher("/token/**"),
                                        new AntPathRequestMatcher("/v1/cal/**"),
                                        new AntPathRequestMatcher("/v1/post/**"),
                                        new AntPathRequestMatcher("/v1/blog/**"))
                                .hasAnyRole("USER", "ADMIN")
                                .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()) // H2 Console 프레임 보호 비활성화
                )
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList(frontUrl));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return PasswordEncoderFactories.createDelegatingPasswordEncoder(); }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JuneberryAuthenticationProvider juneberryAuthenticationProvider() {
        return new JuneberryAuthenticationProvider();
    }
}
