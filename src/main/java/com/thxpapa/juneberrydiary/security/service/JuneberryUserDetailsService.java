package com.thxpapa.juneberrydiary.security.service;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.domain.user.SecurityUser;
import com.thxpapa.juneberrydiary.repository.userRepository.JuneberryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JuneberryUserDetailsService implements UserDetailsService {

    private final JuneberryUserRepository juneberryUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        JuneberryUser juneberryUser = juneberryUserRepository.findByEmail(email);

        if (juneberryUser == null) {
            throw new UsernameNotFoundException("UsernameNotfoundException");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new SecurityUser(juneberryUser, roles);
    }
}
