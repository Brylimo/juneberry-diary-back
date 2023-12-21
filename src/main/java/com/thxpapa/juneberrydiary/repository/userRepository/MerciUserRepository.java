package com.thxpapa.juneberrydiary.repository.userRepository;

import com.thxpapa.juneberrydiary.domain.user.MerciUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerciUserRepository extends JpaRepository<MerciUser, Integer> {
    MerciUser findByEmail(String email);
}
