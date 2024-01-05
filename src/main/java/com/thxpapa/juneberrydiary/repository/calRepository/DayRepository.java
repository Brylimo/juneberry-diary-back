package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DayRepository extends JpaRepository<Day, Integer> {

    Day findDayByDate(LocalDate date);
}
