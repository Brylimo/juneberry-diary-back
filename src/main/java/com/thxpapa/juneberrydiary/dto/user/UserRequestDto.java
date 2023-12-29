package com.thxpapa.juneberrydiary.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserRequestDto {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class Register {
        @NotEmpty(message = "성명은 필수 입력값입니다.")
        private String name;
        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String email;
        @NotEmpty(message = "사용자 이름은 필수 입력값입니다.")
        private String username;
        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;
        private String intro;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Login {
        @NotEmpty(message = "사용자 이름은 필수 입력값입니다.")
        private String username;
        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(username, password);
        }
    }
}
