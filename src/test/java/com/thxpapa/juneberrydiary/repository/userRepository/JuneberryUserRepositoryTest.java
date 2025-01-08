package com.thxpapa.juneberrydiary.repository.userRepository;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class JuneberryUserRepositoryTest {
    private final JuneberryUserRepository juneberryUserRepository;

    public JuneberryUserRepositoryTest(
            @Autowired JuneberryUserRepository juneberryUserRepository
    ) {
        this.juneberryUserRepository = juneberryUserRepository;
    }

    @DisplayName("사용자 이름에 맞는 유저를 조회한다.")
    @Test
    void findByUsername() {
        // given
        JuneberryUser user1 = createUser("김준베", "juneberry@jbd.com", "juneberry", "juneberrypassword12");
        JuneberryUser user2 = createUser("김철수", "soo@jbd.com", "soo12", "soosoopassword12");

        juneberryUserRepository.saveAll(List.of(user1, user2));

        // when
        JuneberryUser user = juneberryUserRepository.findByUsername("juneberry").get();

        // then
        assertThat(user)
                .extracting("name", "email", "username", "password")
                .contains("김준베", "juneberry@jbd.com", "juneberry", "juneberrypassword12");
    }

    @DisplayName("이메일에 맞는 유저를 조회한다.")
    @Test
    void findByEmail() {
        // given
        JuneberryUser user1 = createUser("김준베", "juneberry@jbd.com", "juneberry", "juneberrypassword12");
        JuneberryUser user2 = createUser("김철수", "soo@jbd.com", "soo12", "soosoopassword12");

        juneberryUserRepository.saveAll(List.of(user1, user2));

        // when
        JuneberryUser user = juneberryUserRepository.findByEmail("juneberry@jbd.com").get();

        // then
        assertThat(user)
                .extracting("name", "email", "username", "password")
                .contains("김준베", "juneberry@jbd.com", "juneberry", "juneberrypassword12");
    }

    private JuneberryUser createUser(String name, String email, String username, String password) {
        return JuneberryUser.builder()
                .name(name)
                .email(email)
                .username(username)
                .password(password)
                .build();
    }
}