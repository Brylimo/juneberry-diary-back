package com.thxpapa.juneberrydiary.service.user;

import com.thxpapa.juneberrydiary.domain.user.MerciUser;
import com.thxpapa.juneberrydiary.dto.UserRegisterRequestDto;

import java.util.List;

public interface MerciUserService {
    List<MerciUser> getAllMerciUsers();
    MerciUser createMerciUser(UserRegisterRequestDto userRegisterRequestDto);
}
