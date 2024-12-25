package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.user.UserRequestDto;
import com.thxpapa.juneberrydiary.dto.user.UserResponseDto;
import com.thxpapa.juneberrydiary.service.token.RefreshTokenService;
import com.thxpapa.juneberrydiary.service.user.JuneberryUserService;
import com.thxpapa.juneberrydiary.util.ErrorUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/token")
public class TokenController {
    @Value("${app.cookie.secure}")
    private boolean cookieSecure;
    @Value("${app.cookie.sameSite:null}")
    private String cookieSameSite;

    private final JuneberryUserService juneberryUserService;
    private final RefreshTokenService refreshTokenService;
    private final ErrorUtil errorUtil;
    private final ResponseDto responseDto;

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Validated UserRequestDto.Login userLoginRequestDto, HttpServletResponse response, Errors errors) {
        if (errors.hasErrors()) {
            return responseDto.invalidData(errorUtil.flatErrors(errors));
        }

        try {
            UserResponseDto.TokenInfo tokenInfo = juneberryUserService.login(userLoginRequestDto);

            if (tokenInfo == null) {
                return responseDto.fail("login failed.", HttpStatus.BAD_REQUEST);
            }

            ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("access_token", tokenInfo.getAccessToken())
                            .path("/")
                            .httpOnly(true)
                            .maxAge(7 * 24 * 60 * 60 * 1000);

            if (cookieSecure) {
                builder = builder.secure(true);
            }

            if (!cookieSameSite.equals("null") && StringUtils.hasText(cookieSameSite)) {
                builder = builder.sameSite(cookieSameSite);
            }

            ResponseCookie cookie = builder.build();
            response.addHeader("Set-Cookie", cookie.toString());

            return responseDto.success(tokenInfo.getUserInfo());
        } catch (Exception e) {
            log.debug("login error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
    @GetMapping(value="/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(
            @Parameter(description = "엑세스 토큰")
            @CookieValue(name="access_token", required = false) String atkCookieValue,
            HttpServletResponse response) {
        if (atkCookieValue != null) {
            refreshTokenService.removeRefreshToken(atkCookieValue);

            ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("access_token", atkCookieValue)
                            .maxAge(Duration.ZERO)
                            .path("/")
                            .httpOnly(true);

            if (cookieSecure) {
                builder = builder.secure(true);
            }

            if (!cookieSameSite.equals("null") && StringUtils.hasText(cookieSameSite)) {
                builder = builder.sameSite(cookieSameSite);
            }

            ResponseCookie atkCookie = builder.build();
            response.addHeader("Set-Cookie", atkCookie.toString());

            return responseDto.success("logout success ");
        }

        return responseDto.fail("logout failed", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "검증", description = "현재 로그인되어 있는 상태인지를 확인합니다.")
    @GetMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validate(@AuthenticationPrincipal JuneberryUser juneberryUser) {
        Boolean hasBlog = false;
        if (juneberryUser.getBlogUsers().size() > 0) hasBlog = true;

        return responseDto.success(UserResponseDto.UserInfo.builder()
                        .name(juneberryUser.getName())
                        .username(juneberryUser.getUsername())
                        .email(juneberryUser.getEmail())
                        .intro(juneberryUser.getIntro())
                        .hasBlog(hasBlog)
                        .build());
    }
}
