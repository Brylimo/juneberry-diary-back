package com.thxpapa.juneberrydiary.service.user;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.user.UserRegisterRequestDto;

import java.util.List;

public interface JuneberryUserService {
    List<JuneberryUser> getAllJuneberryUsers();
    JuneberryUser getByCredentials(final String username, final String password);
    JuneberryUser createJuneberryUser(UserRegisterRequestDto userRegisterRequestDto);
}
