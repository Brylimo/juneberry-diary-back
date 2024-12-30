package com.thxpapa.juneberrydiary.service.user;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.user.UserRequestDto;
import com.thxpapa.juneberrydiary.dto.user.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface JuneberryUserService {
    List<JuneberryUser> getAllJuneberryUsers();
    JuneberryUser getByCredentials(final String username, final String password);
    Optional<JuneberryUser> getByEmail(String email);
    JuneberryUser createJuneberryUser(UserRequestDto.Register userRegisterRequestDto);
    UserResponseDto.TokenInfo login(UserRequestDto.Login userLoginRequestDto);
}
