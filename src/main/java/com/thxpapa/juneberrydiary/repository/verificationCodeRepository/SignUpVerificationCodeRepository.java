package com.thxpapa.juneberrydiary.repository.verificationCodeRepository;

import com.thxpapa.juneberrydiary.domain.verificationCode.SignUpVerificationCode;
import org.springframework.data.repository.CrudRepository;

public interface SignUpVerificationCodeRepository extends CrudRepository<SignUpVerificationCode, String> {
}
