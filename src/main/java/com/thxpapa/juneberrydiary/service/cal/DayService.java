package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayService {
    Optional<Day> createDay(JuneberryUser user, LocalDate date);

    Optional<Day> findOneDay(JuneberryUser user, LocalDate date);
    Optional<Day> storeEventTagList(JuneberryUser user, LocalDate date, List<String> eventTagList);
}