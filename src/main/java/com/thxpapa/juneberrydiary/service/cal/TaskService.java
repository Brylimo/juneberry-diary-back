package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.cal.Task;
import com.thxpapa.juneberrydiary.dto.cal.TaskUpdateDto;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    Task createTask(Day day, String task, String eventCd, int reward);
    List<Task> getTasksByDay(Day day);
    Task updateTask(TaskUpdateDto taskUpdateDto);
    void deleteById(int id);
    List<Task> getEventsByMonth(LocalDate startDate, LocalDate endDate);
    Integer getTodayScore(Day day);
}
