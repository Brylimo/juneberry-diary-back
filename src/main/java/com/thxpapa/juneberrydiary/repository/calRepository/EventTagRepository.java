package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.EventTag;
import com.thxpapa.juneberrydiary.domain.cal.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventTagRepository extends JpaRepository<EventTag, Integer> {
    @Query("SELECT t FROM Task t JOIN FETCH t.day WHERE t.eventCd= :eventCd AND t.day.date BETWEEN :startDate AND :endDate ORDER BY t.modDt ASC")
    List<Task> findAllEventTagsByDate(@Param("eventCd") String eventCd, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
