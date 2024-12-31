package com.thxpapa.juneberrydiary.web.v1;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.user.UserRequestDto;
import com.thxpapa.juneberrydiary.dto.user.UserResponseDto;
import com.thxpapa.juneberrydiary.service.user.JuneberryUserService;
import com.thxpapa.juneberrydiary.service.verificationCode.VerificationCodeService;
import com.thxpapa.juneberrydiary.util.ErrorUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/user")
public class UserController {
    private final JuneberryUserService juneberryUserService;
    private final VerificationCodeService verificationCodeService;
    private final ErrorUtil errorUtil;
    private final ResponseDto responseDto;

    @Operation(summary = "사용자 저장", description = "새로 생성된 사용자를 저장합니다.")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> join(@RequestBody @Validated UserRequestDto.Register userRegisterRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return responseDto.invalidData(errorUtil.flatErrors(errors));
        }

        try {
            JuneberryUser juneberryUser = juneberryUserService.createJuneberryUser(userRegisterRequestDto);

            if (juneberryUser == null) {
                return responseDto.fail("can't register user", HttpStatus.BAD_REQUEST);
            }

            return responseDto.success(UserResponseDto.UserInfo.builder()
                    .name(juneberryUser.getName())
                    .username(juneberryUser.getUsername())
                    .email(juneberryUser.getEmail())
                    .intro(juneberryUser.getIntro())
                    .build());
        } catch (Exception e) {
            log.debug("join error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "사용자 이메일 조회", description = "이메일을 이용해 사용자를 조회합니다.")
    @GetMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByEmail(
            @Parameter(description = "이메일")
            @RequestParam("email") String email) {
        try {
            Optional<JuneberryUser> optionalJuneberryUser = juneberryUserService.getByEmail(email);

            if (optionalJuneberryUser.isEmpty()) {
                return responseDto.fail("유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
            } else {
                return responseDto.success();
            }
        } catch (Exception e) {
            log.debug("getUserByEmail error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "회원가입 인증코드 검증", description = "회원가입시 이메일 인증코드를 검증합니다.")
    @PostMapping(value = "/verification-code", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyCode(@RequestBody UserRequestDto.VerifyCode verifyCode) {
        try {
            boolean isVerified = verificationCodeService.verifySignUpVerificationCode(verifyCode);

            if (isVerified) {
                return responseDto.success();
            } else {
                return responseDto.fail("인증코드가 맞지 않습니다.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.debug("verificationCode error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
