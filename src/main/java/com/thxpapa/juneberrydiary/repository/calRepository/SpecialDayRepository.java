package com.thxpapa.juneberrydiary.repository.calRepository;

import com.thxpapa.juneberrydiary.domain.cal.SpecialDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SpecialDayRepository extends JpaRepository<SpecialDay, String> {
    @Query("SELECT s.specialDayUid FROM SpecialDay s WHERE CAST(SUBSTRING(s.specialDayUid, LOCATE('::', s.specialDayUid) + 2) AS INTEGER) = (SELECT MAX(CAST(SUBSTRING(s2.specialDayUid, LOCATE('::', s2.specialDayUid) + 2) AS INTEGER)) FROM SpecialDay s2)")
    String selectLastId();

    @Query(value = "SELECT MAX(s.specialDayUid) FROM SpecialDay s WHERE s.datStId = :datStId")
    String selectLastIdByDatStId(@Param("datStId") String datStId);

    List<SpecialDay> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
