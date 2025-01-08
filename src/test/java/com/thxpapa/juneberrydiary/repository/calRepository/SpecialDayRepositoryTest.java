package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.SpecialDay;
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
class SpecialDayRepositoryTest {
    private final SpecialDayRepository specialDayRepository;

    public SpecialDayRepositoryTest(
        @Autowired SpecialDayRepository specialDayRepository
    ) {
        this.specialDayRepository = specialDayRepository;
    }

    @DisplayName("가장 나중에 저장된 데이터의 아이디를 조회한다.")
    @Test
    void selectLastId() {
        // given
        specialDayRepository.save(createSpecialDay("20250105::1002", LocalDate.of(2025, 1, 5), "24DIVISIONS"));
        specialDayRepository.save(createSpecialDay("20250120::1003", LocalDate.of(2025, 1, 20), "24DIVISIONS"));

        // when
        String lastId = specialDayRepository.selectLastId();

        // then
        assertThat(lastId).isEqualTo("20250120::1003");
    }

    @DisplayName("데이터셋 식별자를 기준으로 가장 나중에 저장된 데이터의 아이디를 조회한다.")
    @Test
    void selectLastIdByDatStId() {
        // given
        specialDayRepository.save(createSpecialDay("20250105::1002", LocalDate.of(2025, 1, 5), "24DIVISIONS"));
        specialDayRepository.save(createSpecialDay("20250120::1003", LocalDate.of(2025, 1, 20), "24DIVISIONS"));
        specialDayRepository.save(createSpecialDay("20250203::1004", LocalDate.of(2025, 2, 3), "HOLIDAY"));

        // when
        String lastId = specialDayRepository.selectLastIdByDatStId("24DIVISIONS");

        // then
        assertThat(lastId).isEqualTo("20250120::1003");
    }

    @DisplayName("날짜 범위를 기준으로 special day를 조회한다.")
    @Test
    void findAllByDateBetween() {
        // given
        specialDayRepository.save(createSpecialDay("20250105::1002", LocalDate.of(2025, 1, 5), "24DIVISIONS"));
        specialDayRepository.save(createSpecialDay("20250120::1003", LocalDate.of(2025, 1, 20), "24DIVISIONS"));
        specialDayRepository.save(createSpecialDay("20250121::1004", LocalDate.of(2025, 1, 21), "24DIVISIONS"));
        specialDayRepository.save(createSpecialDay("20250122::1005", LocalDate.of(2025, 1, 22), "24DIVISIONS"));
        specialDayRepository.save(createSpecialDay("20250203::1006", LocalDate.of(2025, 2, 3), "HOLIDAY"));

        LocalDate start = LocalDate.of(2025, 1, 19);
        LocalDate end = LocalDate.of(2025, 1, 23);

        // when
        List<SpecialDay> specialDayList = specialDayRepository.findAllByDateBetween(start, end);

        // then
        assertThat(specialDayList).hasSize(3)
                .extracting(SpecialDay::getSpecialDayUid)
                .containsExactly("20250120::1003", "20250121::1004", "20250122::1005");
    }

    private SpecialDay createSpecialDay(String specialDayUid, LocalDate date, String datStId) {
        return SpecialDay.builder()
                .specialDayUid(specialDayUid)
                .date(date)
                .dateName("처서")
                .datStId(datStId)
                .build();
    }
}