package com.thxpapa.juneberrydiary.repository.authRepository;

import com.thxpapa.juneberrydiary.domain.auth.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByAccessToken(String accessToken);
}
