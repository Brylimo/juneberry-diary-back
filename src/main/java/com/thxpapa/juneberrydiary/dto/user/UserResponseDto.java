package com.thxpapa.juneberrydiary.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpirationTime;
        private Long refreshTokenExpirationTime;
        private UserInfo userInfo;

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private String name;
        private String email;
        private String username;
        private String intro;
        private Boolean hasBlog;
    }
}
