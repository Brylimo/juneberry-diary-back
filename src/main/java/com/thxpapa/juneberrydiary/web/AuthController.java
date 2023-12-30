package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.Response;
import com.thxpapa.juneberrydiary.dto.user.UserRequestDto;
import com.thxpapa.juneberrydiary.dto.user.UserResponseDto;
import com.thxpapa.juneberrydiary.security.provider.TokenProvider;
import com.thxpapa.juneberrydiary.service.user.JuneberryUserService;
import com.thxpapa.juneberrydiary.util.ErrorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final JuneberryUserService juneberryUserService;
    private final TokenProvider tokenProvider;
    private final ErrorUtil errorUtil;
    private final Response response;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> join(@RequestBody @Validated  UserRequestDto.Register userRegisterRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidData(errorUtil.flatErrors(errors));
        }

        try {
            JuneberryUser juneberryUser = juneberryUserService.createJuneberryUser(userRegisterRequestDto);

            if (juneberryUser == null) {
                return response.fail("can't register user", HttpStatus.BAD_REQUEST);
            }

            return response.success(juneberryUser);
        } catch (Exception e) {
            log.debug("register error occurred!");
            return response.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Validated  UserRequestDto.Login userLoginRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidData(errorUtil.flatErrors(errors));
        }

        try {
            UserResponseDto.TokenInfo tokenInfo = juneberryUserService.login(userLoginRequestDto);

            if (tokenInfo == null) {
                return response.fail("login failed.", HttpStatus.BAD_REQUEST);
            }

            return response.success(tokenInfo);
        } catch (Exception e) {
            log.debug("login error occurred!");
            return response.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@PostMapping(value="/reissue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reissue(@RequestBody @Validated UserRequestDto.Reissue userReissueRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidData(errorUtil.flatErrors(errors));
        }

        try {
            if (!tokenProvider.validateToken(userReissueRequestDto.getRefreshToken())) {
                return response.fail("refresh token isn't valid", HttpStatus.BAD_REQUEST);
            }


            return null;
        } catch (Exception e) {
            log.debug("login error occurred!");
            return response.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
}
