package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.SpecialDay;
import com.thxpapa.juneberrydiary.dto.cal.CalResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface SpecialDayService {
    void updateHoliday();
    void updateAnniversary();
    void update24Divisions();
    List<CalResponseDto.SpecialDayInfo> getSpecialDaysByMonth(LocalDate startDate, LocalDate endDate);
}
