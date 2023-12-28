package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.domain.user.SecurityUser;
import com.thxpapa.juneberrydiary.dto.ErrorResponse;
import com.thxpapa.juneberrydiary.dto.user.UserDto;
import com.thxpapa.juneberrydiary.dto.user.UserRegisterRequestDto;
import com.thxpapa.juneberrydiary.security.provider.TokenProvider;
import com.thxpapa.juneberrydiary.service.user.JuneberryUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final JuneberryUserService juneberryUserService;
    private final TokenProvider tokenProvider;
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> join(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        try {
            JuneberryUser juneberryUser = juneberryUserService.createJuneberryUser(userRegisterRequestDto);

            if (juneberryUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("can't register user"));
            }

            return ResponseEntity.status(HttpStatus.OK).body(juneberryUser);
        } catch (Exception e) {
            log.debug("register error occurred!");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("server error"));
        }
    }

    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody UserDto userDto) {
        try {
            JuneberryUser user = juneberryUserService.getByCredentials(userDto.getUsername(), userDto.getPassword());

            if (user != null) {
                final String token = tokenProvider.create(user);
                final UserDto res = UserDto.builder()
                        .username(user.getUsername())
                        .id(user.getJuneberryUserUid())
                        .token(token)
                        .build();
                return ResponseEntity.ok().body(res);
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("Login failed."));
            }
        } catch (Exception e) {
            log.debug("login error occurred!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("server error"));
        }
    }

    @GetMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> check(@AuthenticationPrincipal SecurityUser user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    /*@GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(req, res, auth);
        }

        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }*/



}
