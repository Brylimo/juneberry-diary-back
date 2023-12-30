package com.thxpapa.juneberrydiary.service.user;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.user.UserRequestDto;
import com.thxpapa.juneberrydiary.dto.user.UserResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JuneberryUserService {
    List<JuneberryUser> getAllJuneberryUsers();
    JuneberryUser getByCredentials(final String username, final String password);
    JuneberryUser createJuneberryUser(UserRequestDto.Register userRegisterRequestDto);
    UserResponseDto.TokenInfo login(UserRequestDto.Login userLoginRequestDto);
    UserResponseDto.TokenInfo reissue(UserRequestDto.Reissue userReissueRequestDto);
}
