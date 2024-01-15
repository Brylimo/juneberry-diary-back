package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.repository.calRepository.DayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DayServiceImpl implements DayService {

    private final DayRepository dayRepository;

    @Override
    public Optional<Day> createDay(LocalDate date) {
        Day createdDay = dayRepository.save(Day.builder()
                                .date(date)
                                .build());

        return Optional.ofNullable(createdDay);
    }

    @Override
    public Optional<Day> findOneDay(LocalDate date) {
        return dayRepository.findDayByDate(date);
    }
}
