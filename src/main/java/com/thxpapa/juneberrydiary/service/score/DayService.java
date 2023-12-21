package com.thxpapa.juneberrydiary.service.score;

import com.thxpapa.juneberrydiary.domain.score.Day;

import java.time.LocalDate;

public interface DayService {
    Day createDay(LocalDate date);

    Day findOneDay(LocalDate date);
}