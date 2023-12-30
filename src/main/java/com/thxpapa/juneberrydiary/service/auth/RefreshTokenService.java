package com.thxpapa.juneberrydiary.service.auth;

public interface RefreshTokenService {
    void writeTokenInfo(String username, String refreshToken, String accessToken);
    void removeRefreshToken(String accessToken);
}
