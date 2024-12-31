package com.thxpapa.juneberrydiary.service.verificationCode;

import com.thxpapa.juneberrydiary.dto.user.UserRequestDto;

public interface VerificationCodeService {
    void storeSignUpVerificationCode(String email, String code);
    boolean verifySignUpVerificationCode(UserRequestDto.VerifyCode verifyCode);
}
