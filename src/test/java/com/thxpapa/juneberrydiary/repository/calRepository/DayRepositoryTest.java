package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.Day;
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
class DayRepositoryTest {
    private final DayRepository dayRepository;
    private final JuneberryUserRepository juneberryUserRepository;
    private JuneberryUser testUser;

    public DayRepositoryTest(
            @Autowired DayRepository dayRepository,
            @Autowired JuneberryUserRepository juneberryUserRepository
    ) {
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

        dayRepository.save(createDay(testUser, LocalDate.of(2025, 1, 1), "Happy New Year!"));
        dayRepository.save(createDay(testUser, LocalDate.of(2025, 1, 5), "Went hiking."));
        dayRepository.save(createDay(testUser, LocalDate.of(2025, 1, 10), "Watched a movie."));
    }

    @DisplayName("유저와 날짜 범위를 기준으로 day를 조회한다.")
    @Test
    void findDayByJuneberryUserAndDateBetween() {
        // given
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 7);

        // when
        List<Day> dayList = dayRepository.findDayByJuneberryUserAndDateBetween(testUser, start, end);

        // then
        assertThat(dayList).hasSize(2)
                .extracting(Day::getTodayTxt)
                .containsExactlyInAnyOrder("Happy New Year!", "Went hiking.");
    }

    @DisplayName("유저와 일자를 기준으로 day를 조회한다.")
    @Test
    void findFirstDayByDateAndJuneberryUser() {
        // given
        LocalDate date = LocalDate.of(2025, 1, 1);

        // when
        Day day = dayRepository.findFirstDayByDateAndJuneberryUser(date, testUser).get();

        // then
        assertThat(day.getTodayTxt()).isEqualTo("Happy New Year!");
    }

    private Day createDay(JuneberryUser user, LocalDate date, String todayTxt) {
        return Day.builder()
                .juneberryUser(user)
                .date(date)
                .todayTxt(todayTxt)
                .emojiCodes("1234")
                .build();
    }
}