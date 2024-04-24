package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.cal.Todo;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.cal.CalRequestDto;
import com.thxpapa.juneberrydiary.dto.cal.CalResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoService {
    Optional<Todo> createTodoByTodoLine(JuneberryUser user, LocalDate date, CalRequestDto.TodoLine todoLine);
    List<CalResponseDto.TodoInfo> getTodosByDate(JuneberryUser user, LocalDate date);
    Optional<Todo> getTodoByPosition(Day day, int position);
    void updateTodoChk(JuneberryUser user, LocalDate date, CalRequestDto.TodoChk calTodoChkDto);
    void deleteById(int id);
    List<Todo> getEventsByMonth(LocalDate startDate, LocalDate endDate);
    Integer getTodayScore(Day day);
}
