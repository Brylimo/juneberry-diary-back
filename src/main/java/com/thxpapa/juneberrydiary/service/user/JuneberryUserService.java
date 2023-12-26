package com.thxpapa.juneberrydiary.service.user;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.UserRegisterRequestDto;

import java.util.List;

public interface JuneberryUserService {
    List<JuneberryUser> getAllJuneberryUsers();
    JuneberryUser createJuneberryUser(UserRegisterRequestDto userRegisterRequestDto);
}
