package com.thxpapa.juneberrydiary.service.user;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.user.UserRequestDto;
import com.thxpapa.juneberrydiary.dto.user.UserResponseDto;
import com.thxpapa.juneberrydiary.enums.Authority;
import com.thxpapa.juneberrydiary.repository.userRepository.JuneberryUserRepository;
import com.thxpapa.juneberrydiary.security.provider.TokenProvider;
import com.thxpapa.juneberrydiary.service.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JuneberryUserServiceImpl implements JuneberryUserService {

    private final PasswordEncoder passwordEncoder;

    private final JuneberryUserRepository juneberryUserRepository;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @Override
    public List<JuneberryUser> getAllJuneberryUsers() {
        List<JuneberryUser> juneberryUsers = juneberryUserRepository.findAll();
        return juneberryUsers;
    }

    @Override
    public JuneberryUser getByCredentials(final String username, final String password) {
        final JuneberryUser user = juneberryUserRepository.findByUsername(username).orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public JuneberryUser createJuneberryUser(UserRequestDto.Register userRegisterRequestDto) {
        JuneberryUser createdJuneberryUser =  juneberryUserRepository.save(JuneberryUser.builder()
                                            .name(userRegisterRequestDto.getName())
                                            .email(userRegisterRequestDto.getEmail())
                                            .username(userRegisterRequestDto.getUsername())
                                            .password(passwordEncoder.encode(userRegisterRequestDto.getPassword()))
                                            .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                                            .intro(userRegisterRequestDto.getIntro())
                                            .build());
        return createdJuneberryUser;
    }

    @Override
    public UserResponseDto.TokenInfo login(UserRequestDto.Login userLoginRequestDto) {
        JuneberryUser user = getByCredentials(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword());

        if (user == null) {
            return null;
        }

        UsernamePasswordAuthenticationToken authenticationToken = userLoginRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken); // 실제 검증
        UserResponseDto.TokenInfo tokenInfo = tokenProvider.generateTokens(authentication);

        Boolean hasBlog = false;
        if (user.getBlogUsers().size() > 0) hasBlog = true;

        tokenInfo.setUserInfo(UserResponseDto.UserInfo.builder()
                        .name(user.getName())
                        .username(user.getUsername())
                        .intro(user.getIntro())
                        .hasBlog(hasBlog)
                        .build());

        refreshTokenService.writeTokenInfo(user.getUsername(), tokenInfo.getRefreshToken(), tokenInfo.getAccessToken());
        return tokenInfo;
    }
}