package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.cal.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByTodoUid(Long id);
    List<Todo> findTodoByDayOrderByPositionAsc(Day day);
    Optional<Todo> findFirstTodoByDayAndPosition(Day day, int position);

/*    @Query("SELECT t FROM Todo t JOIN FETCH t.day WHERE t.eventCd= :eventCd AND t.day.date BETWEEN :startDate AND :endDate ORDER BY t.modDt ASC")
    List<Todo> findAllEventsByDate(@Param("eventCd") String eventCd, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(t.reward) FROM Todo t WHERE t.eventCd = '00' AND t.doneCd = true AND t.day = :day")
    Integer sumRewardsByDay(@Param("day") Day day);*/
}
