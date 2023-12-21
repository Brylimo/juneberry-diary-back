package com.thxpapa.juneberrydiary.service.user;

import com.thxpapa.juneberrydiary.domain.user.MerciUser;
import com.thxpapa.juneberrydiary.dto.UserRegisterRequestDto;
import com.thxpapa.juneberrydiary.repository.userRepository.MerciUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerciUserServiceImpl implements MerciUserService {

    private final PasswordEncoder passwordEncoder;

    private final MerciUserRepository merciUserRepository;

    @Override
    public List<MerciUser> getAllMerciUsers() {
        List<MerciUser> merciUsers = merciUserRepository.findAll();
        return merciUsers;
    }

    @Override
    public MerciUser createMerciUser(UserRegisterRequestDto userRegisterRequestDto) {
        MerciUser createdMerciUser =  merciUserRepository.save(MerciUser.builder()
                                            .name(userRegisterRequestDto.getName())
                                            .email(userRegisterRequestDto.getEmail())
                                            .username(userRegisterRequestDto.getUsername())
                                            .password(passwordEncoder.encode(userRegisterRequestDto.getPassword()))
                                            .intro(userRegisterRequestDto.getIntro())
                                            .statusCd("01")
                                            .build());
        return createdMerciUser;
    }
}
