package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.repository.calRepository.DayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DayServiceImpl implements DayService {

    private final DayRepository dayRepository;

    @Override
    @Transactional
    public Optional<Day> createDay(JuneberryUser user, LocalDate date) {
        Day createdDay = dayRepository.save(Day.builder()
                                .date(date)
                                .juneberryUser(user)
                                .build());

        return Optional.ofNullable(createdDay);
    }

    @Override
    @Transactional
    public Optional<Day> findOneDay(JuneberryUser user, LocalDate date) {
        return dayRepository.findDayByDateAndJuneberryUser(date, user);
    }

    @Override
    @Transactional
    public Optional<Day> storeEventTagList(JuneberryUser user, LocalDate date, List<String> eventTagList) {
        Optional<Day> optionalDay = findOneDay(user, date);

        Day day = optionalDay.orElseGet(() -> createDay(user, date).orElseThrow());
        day.updateEventTagList(eventTagList);

        return Optional.ofNullable(day);
    }
}
