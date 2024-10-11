package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayRepository extends JpaRepository<Day, Long> {
    List<Day> findDayByJuneberryUserAndDateBetween(JuneberryUser user, LocalDate start, LocalDate end);
    Optional<Day> findFirstDayByDateAndJuneberryUser(LocalDate date, JuneberryUser user);
}
