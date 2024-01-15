package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;

import java.time.LocalDate;
import java.util.Optional;

public interface DayService {
    Optional<Day> createDay(LocalDate date);

    Optional<Day> findOneDay(LocalDate date);
}