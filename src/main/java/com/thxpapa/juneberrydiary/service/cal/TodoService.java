package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.cal.Todo;
import com.thxpapa.juneberrydiary.dto.cal.TodoUpdateDto;

import java.time.LocalDate;
import java.util.List;

public interface TodoService {
    Todo createTodo(Day day, String task, int reward);
    List<Todo> getTodosByDay(Day day);
    Todo updateTodo(TodoUpdateDto todoUpdateDto);
    void deleteById(int id);
    List<Todo> getEventsByMonth(LocalDate startDate, LocalDate endDate);
    Integer getTodayScore(Day day);
}
