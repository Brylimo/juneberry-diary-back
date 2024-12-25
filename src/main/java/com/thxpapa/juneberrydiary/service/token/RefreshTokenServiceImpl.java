package com.thxpapa.juneberrydiary.service.token;

import com.thxpapa.juneberrydiary.domain.token.RefreshToken;
import com.thxpapa.juneberrydiary.repository.authRepository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void writeTokenInfo(String username, String refreshToken, String accessToken) {
        refreshTokenRepository.save(RefreshToken.builder()
                        .username(username)
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .build());
    }

    @Override
    @Transactional
    public void removeRefreshToken(String accessToken) {
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
    }
}
