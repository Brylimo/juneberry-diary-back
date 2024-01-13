package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.EventTag;

import java.time.LocalDate;
import java.util.List;

public interface EventTagService {
    List<EventTag> getEventTagsByMonth(LocalDate startDate, LocalDate endDate);
}
