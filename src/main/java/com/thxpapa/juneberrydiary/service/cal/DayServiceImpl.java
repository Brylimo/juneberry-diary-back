package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.cal.CalResponseDto;
import com.thxpapa.juneberrydiary.repository.calRepository.DayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DayServiceImpl implements DayService {

    private final DayRepository dayRepository;

    @Override
    @Transactional
    public Day createDay(JuneberryUser user, LocalDate date) {
        Day createdDay = dayRepository.save(Day.builder()
                                .date(date)
                                .eventTagList("")
                                .juneberryUser(user)
                                .build());

        return createdDay;
    }

    @Override
    @Transactional
    public Optional<Day> findOneDay(JuneberryUser user, LocalDate date) {
        return dayRepository.findFirstDayByDateAndJuneberryUser(date, user);
    }

    @Override
    @Transactional
    public List<CalResponseDto.EventDayInfo> findEventDayByMonth(JuneberryUser user, LocalDate startDate, LocalDate endDate) {
        Optional<List<Day>> optionalDays = dayRepository.findDayByJuneberryUserAndDateBetween(user, startDate, endDate);

        Optional<List<CalResponseDto.EventDayInfo>> eventTagsInfoList = optionalDays.map(days ->
                days.stream().filter(day -> {
                    if (day.getEventTags() != null && !day.getEventTags().isEmpty()) return true;
                    else return false;
                }).map(day -> {
                    String[] tags = day.getEventTags().split(",");

                    return CalResponseDto.EventDayInfo.builder()
                            .date(day.getDate())
                            .eventTags(Arrays.asList(tags))
                            .build();
                }).collect(Collectors.toList()));

        return eventTagsInfoList.orElseGet(()->new ArrayList<>());
    }

    @Override
    @Transactional
    public List<CalResponseDto.EmojiInfo> findEmojiByMonth(JuneberryUser user, LocalDate startDate, LocalDate endDate) {
        Optional<List<Day>> optionalDays = dayRepository.findDayByJuneberryUserAndDateBetween(user, startDate, endDate);

        Optional<List<CalResponseDto.EmojiInfo>> emojiInfoList = optionalDays.map(days ->
                days.stream().filter(day -> {
                    if (day.getEmojiCodes() != null && !day.getEmojiCodes().isEmpty()) return true;
                    else return false;
                }).map(day -> {
                    String[] emojiCodes = day.getEmojiCodes().split(",");

                    return CalResponseDto.EmojiInfo.builder()
                            .date(day.getDate())
                            .emojiCodeArray(Arrays.asList(emojiCodes))
                            .build();
                }).collect(Collectors.toList()));

        return emojiInfoList.orElseGet(()->new ArrayList<>());
    }

    @Override
    @Transactional
    public Optional<String> findTodayTxt(JuneberryUser user, LocalDate date) {
        return findOneDay(user, date).map(Day::getTodayTxt);
    }

    @Override
    @Transactional
    public Day storeEventTagList(JuneberryUser user, LocalDate date, List<String> eventTagList) {
        Optional<Day> optionalDay = findOneDay(user, date);

        Day day = optionalDay.orElseGet(() -> createDay(user, date));
        day.updateEventTagList(Optional.ofNullable(eventTagList));

        return day;
    }

    @Override
    @Transactional
    public Day storeTodayTxt(JuneberryUser user, LocalDate date, String todayTxt) {
        Optional<Day> optionalDay = findOneDay(user, date);

        Day day = optionalDay.orElseGet(() -> createDay(user, date));
        day.updateTodayTxt(Optional.ofNullable(todayTxt));

        return day;
    }

    @Override
    @Transactional
    public Day storeDayEmoji(JuneberryUser user, LocalDate date, List<String> emojiCodeList) {
        Optional<Day> optionalDay = findOneDay(user, date);

        Day day = optionalDay.orElseGet(() -> createDay(user, date));
        day.updateEmojiCodes(Optional.ofNullable(emojiCodeList));

        return day;
    }
}
