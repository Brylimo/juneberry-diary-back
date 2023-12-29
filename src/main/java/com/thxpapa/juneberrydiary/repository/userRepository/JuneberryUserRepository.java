package com.thxpapa.juneberrydiary.repository.userRepository;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JuneberryUserRepository extends JpaRepository<JuneberryUser, Integer> {
    JuneberryUser findByEmail(String email);
    JuneberryUser findByJuneberryUserUid(int juneberryUserUid);
    Optional<JuneberryUser> findByUsername(String username);
}
