package com.thxpapa.juneberrydiary.security.service;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.repository.userRepository.JuneberryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JuneberryUserDetailsService implements UserDetailsService {

    private final JuneberryUserRepository juneberryUserRepository;

    @Override
    @Transactional
    public JuneberryUser loadUserByUsername(String username) throws UsernameNotFoundException {

        return juneberryUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }
}
