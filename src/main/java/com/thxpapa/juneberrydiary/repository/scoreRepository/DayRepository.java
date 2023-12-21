package com.thxpapa.juneberrydiary.repository.scoreRepository;

import com.thxpapa.juneberrydiary.domain.score.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DayRepository extends JpaRepository<Day, Integer> {

    Day findDayByDate(LocalDate date);
}
