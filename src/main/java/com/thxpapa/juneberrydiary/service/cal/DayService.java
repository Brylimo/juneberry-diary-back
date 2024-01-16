package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.cal.CalResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayService {
    Optional<Day> createDay(JuneberryUser user, LocalDate date);

    Optional<Day> findOneDay(JuneberryUser user, LocalDate date);
    Optional<List<CalResponseDto.EventDayInfo>> findEventDayByMonth(JuneberryUser user, LocalDate startDate, LocalDate endDate);
    Optional<Day> storeEventTagList(JuneberryUser user, LocalDate date, List<String> eventTagList);
}