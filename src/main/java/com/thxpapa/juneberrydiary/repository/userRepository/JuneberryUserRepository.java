package com.thxpapa.juneberrydiary.repository.userRepository;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JuneberryUserRepository extends JpaRepository<JuneberryUser, Long> {
    Optional<JuneberryUser> findByUsername(String username);
    Optional<JuneberryUser> findFirstByEmail(String email);
}
