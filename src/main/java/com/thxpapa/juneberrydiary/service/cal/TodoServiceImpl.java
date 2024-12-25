package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.cal.Todo;
import com.thxpapa.juneberrydiary.domain.cal.TodoGroup;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.cal.CalRequestDto;
import com.thxpapa.juneberrydiary.dto.cal.CalResponseDto;
import com.thxpapa.juneberrydiary.repository.calRepository.TodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    @PersistenceContext
    private EntityManager em;

    private final DayService dayService;
    private final TodoGroupService todoGroupService;
    private final TodoRepository todoRepository;

    @Override
    @Transactional
    public Optional<Todo> createTodoByTodoLine(JuneberryUser user, LocalDate date, CalRequestDto.TodoLine todoLine) {
        TodoGroup todoGroup;
        Day day = dayService.findOneDay(user, date).orElseGet(() -> dayService.createDay(user, date));

        todoGroup = todoGroupService.getTodoGroupByName(user, todoLine.getGroupName())
                .orElseGet(()->todoGroupService.createTodoGroup(user, todoLine.getGroupName(), "red"));

        if (StringUtils.hasText(todoLine.getGroupName()) ||
                StringUtils.hasText(todoLine.getContent())) { // content나 group에 값이 쓰여져 있음
            // position을 기준으로 존재여부 확인
            Optional<Todo> optionalTodo = getTodoByPosition(day, todoLine.getPosition());

            if (optionalTodo.isPresent()) { // todo가 이미 존재
                Todo existedTodo = optionalTodo.get();
                existedTodo.updateTodoByTodoLine(todoLine, todoGroup);
                return Optional.of(existedTodo);
            } else { // todo가 존재하지 않음
                Todo createdTodo = todoRepository.save(Todo.builder()
                        .todoGroup(todoGroup)
                        .content(todoLine.getContent())
                        .position(todoLine.getPosition())
                        .chkStatus(todoLine.getChkStatus())
                        .day(day)
                        .build());
                return Optional.of(createdTodo);
            }
        }
        // 투두 content에 값이 쓰여져 있지 않음
        return Optional.empty();
    }

    @Override
    @Transactional
    public List<CalResponseDto.TodoInfo> getTodosByDate(JuneberryUser user, LocalDate date) {
        Day day = dayService.findOneDay(user, date).orElseThrow();
        List<Todo> todoList = todoRepository.findTodoByDayOrderByPositionAsc(day);

        List<CalResponseDto.TodoInfo> todoInfoList = todoList.stream()
                .map(todo -> {
                   return CalResponseDto.TodoInfo.builder()
                               .id(todo.getTodoUid())
                               .date(date.toString())
                               .content(todo.getContent())
                               .position(todo.getPosition())
                               .chkStatus(todo.getChkStatus())
                               .reward(todo.getReward())
                               .groupName(todo.getTodoGroup().getName())
                               .color(todo.getTodoGroup().getColor())
                               .build();
                }).collect(Collectors.toList());

        return todoInfoList;
    }

    @Override
    @Transactional
    public Optional<Todo> getTodoByPosition(Day day, int position) {
        return todoRepository.findFirstTodoByDayAndPosition(day, position);
    }

    @Override
    @Transactional
    public Optional<Todo> updateTodo(Long id, CalRequestDto.TodoLine todoLine) {
        Optional<Todo> optionalTodo = todoRepository.findByTodoUid(id);

        return optionalTodo.map(todo -> {
            todo.updateTodoByTodoLine(todoLine, null);
            return todo;
        });
    }

    @Override
    public void deleteById(Long id) {
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
