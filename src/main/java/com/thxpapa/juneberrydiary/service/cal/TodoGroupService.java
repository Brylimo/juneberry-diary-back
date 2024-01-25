package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.TodoGroup;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;

import java.util.Optional;

public interface TodoGroupService {
    TodoGroup createTodoGroup(JuneberryUser user, String name, String color);
    Optional<TodoGroup> getTodoGroupByName(JuneberryUser user, String name);
}