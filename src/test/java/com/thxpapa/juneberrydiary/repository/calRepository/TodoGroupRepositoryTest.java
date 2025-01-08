package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.TodoGroup;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.repository.userRepository.JuneberryUserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class TodoGroupRepositoryTest {
    private final TodoGroupRepository todoGroupRepository;
    private final JuneberryUserRepository juneberryUserRepository;

    public TodoGroupRepositoryTest(
        @Autowired TodoGroupRepository todoGroupRepository,
        @Autowired JuneberryUserRepository juneberryUserRepository
    ) {
        this.todoGroupRepository = todoGroupRepository;
        this.juneberryUserRepository = juneberryUserRepository;
    }

    @DisplayName("유저와 이름을 통해 TodoGroup을 조회한다.")
    @Transactional
    @Test
    void findFirstByNameAndJuneberryUser() {
        // given
        JuneberryUser testUser = juneberryUserRepository.save(JuneberryUser.builder()
                .name("testUser")
                .email("test@example.com")
                .password("1111")
                .username("juneberry")
                .build());

        todoGroupRepository.save(createTodoGroup(testUser, "집안일"));
        todoGroupRepository.save(createTodoGroup(testUser, "코딩"));
        todoGroupRepository.save(createTodoGroup(testUser, "공부"));

        // when
        TodoGroup todoGroup = todoGroupRepository.findFirstByNameAndJuneberryUser("코딩", testUser).get();

        // then
        assertThat(todoGroup).isNotNull();
        assertThat(todoGroup.getName()).isEqualTo("코딩");
        assertThat(todoGroup.getJuneberryUser()).isEqualTo(testUser);
    }

    private TodoGroup createTodoGroup(JuneberryUser user, String name) {
        return TodoGroup.builder()
                .name(name)
                .color("red")
                .juneberryUser(user)
                .build();
    }
}