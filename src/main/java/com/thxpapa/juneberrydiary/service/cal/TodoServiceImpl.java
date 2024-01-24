package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.cal.Todo;
import com.thxpapa.juneberrydiary.dto.cal.TodoUpdateDto;
import com.thxpapa.juneberrydiary.repository.calRepository.TodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    @PersistenceContext
    private EntityManager em;
    private final TodoRepository todoRepository;

    @Override
    public Todo createTodo(Day day, String content, int reward) {
        Todo createdTask = todoRepository.save(Todo.builder()
                                .content(content)
                                .reward(reward)
                                .day(day)
                                .statusCd("01")
                                .build());

        return createdTask;
    }

    @Override
    public List<Todo> getTodosByDay(Day day) {
        return todoRepository.findTodosByDayOrderByModDtAsc(day);
    }

    @Override
    @Transactional
    public Todo updateTodo(TodoUpdateDto todoUpdateDto) {
        Todo task = todoRepository.findById(todoUpdateDto.getTaskId()).orElse(null);
        return task.updateTodo(em, todoUpdateDto);
    }

    @Override
    public void deleteById(int id) {
        todoRepository.deleteById(id);
    }

    @Override
    public List<Todo> getEventsByMonth(LocalDate startDate, LocalDate endDate) {
        //return todoRepository.findAllEventsByDate("01", startDate, endDate);
        return null;
    }

    @Override
    public Integer getTodayScore(Day day) {
        //return todoRepository.sumRewardsByDay(day);
        return null;
    }
}
