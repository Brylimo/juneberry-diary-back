package com.thxpapa.juneberrydiary.repository.userRepository;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JuneberryUserRepository extends JpaRepository<JuneberryUser, Integer> {
    JuneberryUser findByEmail(String email);
    JuneberryUser findByJuneberryUserUid(int juneberryUserUid);
    JuneberryUser findByUsername(String username);
}
