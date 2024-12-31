package com.thxpapa.juneberrydiary.domain.verificationCode;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value="sign-up-verification-code", timeToLive = 60*5)
@ToString
public class SignUpVerificationCode {
    @Id
    private String email;
    @Indexed
    private String code;

    @Builder
    public SignUpVerificationCode(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
