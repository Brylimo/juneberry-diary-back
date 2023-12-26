package com.thxpapa.juneberrydiary.security.provider;

import com.thxpapa.juneberrydiary.domain.user.SecurityUser;
import com.thxpapa.juneberrydiary.security.service.JuneberryUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class JuneberryAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JuneberryUserDetailsService juneberryUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        // verify email
        SecurityUser securityUser = (SecurityUser) juneberryUserDetailsService.loadUserByUsername(email);

        // verify password
        if (!passwordEncoder.matches(password, securityUser.getJuneberryUser().getPassword())) {
            throw new BadCredentialsException("BadCredentialsException");
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(securityUser.getJuneberryUser(), null, securityUser.getAuthorities());

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
