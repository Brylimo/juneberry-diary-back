package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.TodoGroup;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoGroupRepository extends JpaRepository<TodoGroup, Long> {
    Optional<TodoGroup> findFirstByNameAndJuneberryUser(String name, JuneberryUser user);
}
