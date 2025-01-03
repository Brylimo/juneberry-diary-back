package com.thxpapa.juneberrydiary.repository.userRepository;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
//@SpringBootTest
@DataJpaTest
class JuneberryUserRepositoryTest {
    @Autowired
    private JuneberryUserRepository juneberryUserRepository;

    @DisplayName("사용자 이름에 맞는 유저를 조회한다.")
    @Test
    void findByUsername() {
        // given
        JuneberryUser user1 = JuneberryUser.builder()
                .name("김준베")
                .email("juneberry@jbd.com")
                .username("juneberry")
                .password("juneberrypassword12")
                .build();

        JuneberryUser user2 = JuneberryUser.builder()
                .name("김철수")
                .email("soo@jbd.com")
                .username("soo12")
                .password("soosoopassword12")
                .build();

        juneberryUserRepository.saveAll(List.of(user1, user2));

        // when
        Optional<JuneberryUser> optionalJuneberryUser = juneberryUserRepository.findByUsername("juneberry");

        // then
        assertThat(optionalJuneberryUser)
                .isPresent()
                .get()
                .satisfies(user -> {
                    assertThat(user.getName()).isEqualTo("김준베");
                    assertThat(user.getEmail()).isEqualTo("juneberry@jbd.com");
                    assertThat(user.getUsername()).isEqualTo("juneberry");
                    assertThat(user.getPassword()).isEqualTo("juneberrypassword12");
                });
    }
}