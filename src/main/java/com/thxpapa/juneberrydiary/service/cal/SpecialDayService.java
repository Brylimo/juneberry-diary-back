package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.SpecialDay;

import java.time.LocalDate;
import java.util.List;

public interface SpecialDayService {
    void updateHoliday();
    void updateAnniversary();
    void update24Divisions();
    List<SpecialDay> getSpecialDaysByMonth(LocalDate startDate, LocalDate endDate);
}
