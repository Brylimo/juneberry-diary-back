package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.user.UserRequestDto;
import com.thxpapa.juneberrydiary.dto.user.UserResponseDto;
import com.thxpapa.juneberrydiary.service.auth.RefreshTokenService;
import com.thxpapa.juneberrydiary.service.user.JuneberryUserService;
import com.thxpapa.juneberrydiary.util.ErrorUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final JuneberryUserService juneberryUserService;
    private final RefreshTokenService refreshTokenService;
    private final ErrorUtil errorUtil;
    private final ResponseDto responseDto;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
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
            log.debug("register error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Validated  UserRequestDto.Login userLoginRequestDto, HttpServletResponse response, Errors errors) {
        if (errors.hasErrors()) {
            return responseDto.invalidData(errorUtil.flatErrors(errors));
        }

        try {
            UserResponseDto.TokenInfo tokenInfo = juneberryUserService.login(userLoginRequestDto);

            if (tokenInfo == null) {
                return responseDto.fail("login failed.", HttpStatus.BAD_REQUEST);
            }

            Cookie cookie = new Cookie("access_token", tokenInfo.getAccessToken());
            cookie.setMaxAge(7 * 24 * 60 * 60 * 1000);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return responseDto.success("login success");
        } catch (Exception e) {
            log.debug("login error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@CookieValue("access_token") Cookie atkCookie, HttpServletResponse response) {
        if (atkCookie != null) {
            refreshTokenService.removeRefreshToken(atkCookie.getValue());

            atkCookie.setMaxAge(0);
            atkCookie.setPath("/");
            response.addCookie(atkCookie);;
            return responseDto.success("logout success ");
        }

        return responseDto.fail("logout failed", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validate(@AuthenticationPrincipal JuneberryUser juneberryUser) {
        return responseDto.success(UserResponseDto.UserInfo.builder()
                        .name(juneberryUser.getName())
                        .username(juneberryUser.getUsername())
                        .email(juneberryUser.getEmail())
                        .intro(juneberryUser.getIntro())
                        .build());
    }
}
