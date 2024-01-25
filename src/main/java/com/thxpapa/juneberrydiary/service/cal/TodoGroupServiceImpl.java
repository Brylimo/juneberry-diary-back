package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.TodoGroup;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.repository.calRepository.TodoGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoGroupServiceImpl implements TodoGroupService{
    private final TodoGroupRepository todoGroupRepository;

    @Override
    @Transactional
    public TodoGroup createTodoGroup(JuneberryUser user, String name, String color) {
        TodoGroup todoGroup = todoGroupRepository.save(TodoGroup.builder()
                                        .color(color)
                                        .name(name)
                                        .juneberryUser(user)
                                        .build());
        return todoGroup;
    }

    @Override
    @Transactional
    public Optional<TodoGroup> getTodoGroupByName(JuneberryUser user, String name) {
        return todoGroupRepository.findFirstByNameAndJuneberryUser(name, user);
    }
}
