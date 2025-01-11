package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.cal.Todo;
import com.thxpapa.juneberrydiary.domain.cal.TodoGroup;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.repository.userRepository.JuneberryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class TodoRepositoryTest {
    private final TodoRepository todoRepository;
    private final TodoGroupRepository todoGroupRepository;
    private final DayRepository dayRepository;
    private final JuneberryUserRepository juneberryUserRepository;
    private JuneberryUser testUser;

    public TodoRepositoryTest(
            @Autowired TodoRepository todoRepository,
            @Autowired TodoGroupRepository todoGroupRepository,
            @Autowired DayRepository dayRepository,
            @Autowired JuneberryUserRepository juneberryUserRepository
    ) {
        this.todoRepository = todoRepository;
        this.todoGroupRepository = todoGroupRepository;
        this.dayRepository = dayRepository;
        this.juneberryUserRepository = juneberryUserRepository;
    }

    @BeforeEach
    void setUp() {
        // 테스트용 유저 저장
        testUser = juneberryUserRepository.save(
                JuneberryUser.builder()
                        .name("testUser")
                        .email("test@example.com")
                        .password("1111")
                        .username("juneberry")
                        .build());
    }

    @DisplayName("아이디로 투두를 조회한다.")
    @Test
    void findByTodoUId() {
        // given
        Day day = dayRepository.save(createDay(testUser, LocalDate.of(2025, 1, 1), "Happy New Year!"));
        TodoGroup todoGroup = todoGroupRepository.save(createTodoGroup(testUser, "집안일"));
        Todo todo1 = todoRepository.save(createTodo("빨래", 0, day, todoGroup));
        Todo todo2 = todoRepository.save(createTodo("청소", 1, day, todoGroup));
        Todo todo3 = todoRepository.save(createTodo("쓰레기 버리기", 2, day, todoGroup));

        // when
        Todo todo = todoRepository.findByTodoUid(todo2.getTodoUid()).get();

        // then
        assertThat(todo).isNotNull();
        assertThat(todo.getContent()).isEqualTo("청소");
    }

    @DisplayName("Day에 속한 투두 목록을 조회한다.")
    @Test
    void findTodoByDayOrderByPositionAsc() {
        // given
        Day day = dayRepository.save(createDay(testUser, LocalDate.of(2025, 1, 1), "Happy New Year!"));
        TodoGroup todoGroup = todoGroupRepository.save(createTodoGroup(testUser, "집안일"));
        todoRepository.save(createTodo("빨래", 2, day, todoGroup));
        todoRepository.save(createTodo("청소", 1, day, todoGroup));
        todoRepository.save(createTodo("쓰레기 버리기", 0, day, todoGroup));

        // when
        List<Todo> todoList = todoRepository.findTodoByDayOrderByPositionAsc(day);

        // then
        assertThat(todoList).hasSize(3)
                .extracting("content", "position")
                .containsExactly(
                        tuple("쓰레기 버리기", 0),
                        tuple("청소", 1),
                        tuple("빨래", 2)
                );
    }

    @DisplayName("Day와 Position으로 투두 조회")
    @Test
    void findFirstTodoByDayAndPosition() {
        // given
        Day day = dayRepository.save(createDay(testUser, LocalDate.of(2025, 1, 1), "Happy New Year!"));
        TodoGroup todoGroup = todoGroupRepository.save(createTodoGroup(testUser, "집안일"));
        todoRepository.save(createTodo("빨래", 2, day, todoGroup));
        todoRepository.save(createTodo("청소", 1, day, todoGroup));
        todoRepository.save(createTodo("쓰레기 버리기", 0, day, todoGroup));

        // when
        Todo todo = todoRepository.findFirstTodoByDayAndPosition(day, 1).get();

        // then
        assertThat(todo)
                .extracting("content", "position")
                .contains("청소", 1);
    }

    private Day createDay(JuneberryUser user, LocalDate date, String todayTxt) {
        return Day.builder()
                .juneberryUser(user)
                .date(date)
                .todayTxt(todayTxt)
                .emojiCodes("1234")
                .build();
    }

    private TodoGroup createTodoGroup(JuneberryUser user, String name) {
        return TodoGroup.builder()
                .name(name)
                .color("red")
                .juneberryUser(user)
                .build();
    }

    private Todo createTodo(String content, int position, Day day, TodoGroup todoGroup) {
        return Todo.builder()
                .content(content)
                .reward(10)
                .chkStatus("UNFINISHED")
                .position(position)
                .day(day)
                .todoGroup(todoGroup)
                .build();
    }
}