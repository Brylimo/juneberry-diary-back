package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;

import java.time.LocalDate;

public interface DayService {
    Day createDay(LocalDate date);

    Day findOneDay(LocalDate date);
}