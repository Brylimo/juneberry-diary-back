package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.cal.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findTasksByDayOrderByModDtAsc(Day day);

    @Query("SELECT t FROM Task t JOIN FETCH t.day WHERE t.eventCd= :eventCd AND t.day.date BETWEEN :startDate AND :endDate ORDER BY t.modDt ASC")
    List<Task> findAllEventsByDate(@Param("eventCd") String eventCd, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(t.reward) FROM Task t WHERE t.statusCd = '01' AND t.eventCd = '00' AND t.doneCd = true AND t.day = :day")
    Integer sumRewardsByDay(@Param("day") Day day);
}
