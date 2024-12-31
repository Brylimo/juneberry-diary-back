package com.thxpapa.juneberrydiary.service.verificationCode;

import com.thxpapa.juneberrydiary.domain.verificationCode.SignUpVerificationCode;
import com.thxpapa.juneberrydiary.dto.user.UserRequestDto;
import com.thxpapa.juneberrydiary.repository.verificationCodeRepository.SignUpVerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private final SignUpVerificationCodeRepository signUpVerificationCodeRepository;

    @Override
    public void storeSignUpVerificationCode(String email, String code) {
        try {
            // 이미 인증 코드가 존재한다면 덮어씀
            signUpVerificationCodeRepository.findById(email)
                    .ifPresent(signUpVerificationCode -> signUpVerificationCodeRepository.delete(signUpVerificationCode));

            if (StringUtils.hasText(email)) {
                // 인증 코드 저장
                signUpVerificationCodeRepository.save(SignUpVerificationCode.builder()
                        .email(email)
                        .code(code)
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifySignUpVerificationCode(UserRequestDto.VerifyCode verifyCode) {
        Optional<SignUpVerificationCode> optionalSignUpVerificationCode = signUpVerificationCodeRepository.findById(verifyCode.getEmail());

        if (optionalSignUpVerificationCode.isEmpty()) return false;

        SignUpVerificationCode signUpVerificationCode = optionalSignUpVerificationCode.get();
        if (signUpVerificationCode.getCode().equals(verifyCode.getCode())) {
            // 인증 코드 redis 삭제
            signUpVerificationCodeRepository.delete(signUpVerificationCode);

            return true;
        } else {
            return false;
        }
    }
}
