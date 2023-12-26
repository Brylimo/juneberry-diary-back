package com.thxpapa.juneberrydiary.service.user;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.UserRegisterRequestDto;
import com.thxpapa.juneberrydiary.repository.userRepository.JuneberryUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JuneberryUserServiceImpl implements JuneberryUserService {

    private final PasswordEncoder passwordEncoder;

    private final JuneberryUserRepository juneberryUserRepository;

    @Override
    public List<JuneberryUser> getAllJuneberryUsers() {
        List<JuneberryUser> juneberryUsers = juneberryUserRepository.findAll();
        return juneberryUsers;
    }

    @Override
    public JuneberryUser createJuneberryUser(UserRegisterRequestDto userRegisterRequestDto) {
        JuneberryUser createdJuneberryUser =  juneberryUserRepository.save(JuneberryUser.builder()
                                            .name(userRegisterRequestDto.getName())
                                            .email(userRegisterRequestDto.getEmail())
                                            .username(userRegisterRequestDto.getUsername())
                                            .password(passwordEncoder.encode(userRegisterRequestDto.getPassword()))
                                            .intro(userRegisterRequestDto.getIntro())
                                            .statusCd("01")
                                            .build());
        return createdJuneberryUser;
    }
}
