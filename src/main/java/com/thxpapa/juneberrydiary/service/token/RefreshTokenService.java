package com.thxpapa.juneberrydiary.service.token;

public interface RefreshTokenService {
    void writeTokenInfo(String username, String refreshToken, String accessToken);
    void removeRefreshToken(String accessToken);
}
