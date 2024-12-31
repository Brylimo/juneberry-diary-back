package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.email.EmailRequestDto;
import com.thxpapa.juneberrydiary.dto.email.EmailResponseDto;
import com.thxpapa.juneberrydiary.util.EmailUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/email")
public class EmailController {
    private final EmailUtil emailUtil;
    private final ResponseDto responseDto;

    @Operation(summary = "임시 코드 발송 요청", description = "임시 코드를 이메일을 통해 발송합니다.")
    @PostMapping(value = "/verification-code", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendCode(@RequestBody EmailRequestDto.SendCode sendCode) {
        try {
            EmailResponseDto.EmailMessage emailMessage = EmailResponseDto.EmailMessage.builder()
                    .to(sendCode.getEmail())
                    .subject("[Juneberry Diary]회원가입 이메일 인증 안내")
                    .build();

            emailUtil.sendMail(emailMessage, "email/signup");

            return responseDto.success();
        } catch (Exception e) {
            log.debug("sendCode error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
