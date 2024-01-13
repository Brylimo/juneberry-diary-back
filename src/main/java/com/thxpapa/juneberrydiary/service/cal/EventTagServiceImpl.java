package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.EventTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventTagServiceImpl implements EventTagService{

    @Override
    public List<EventTag> getEventTagsByMonth(LocalDate startDate, LocalDate endDate) {
        return null;
    }
}
