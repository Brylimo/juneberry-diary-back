package com.thxpapa.juneberrydiary.service.cal;

import com.thxpapa.juneberrydiary.domain.cal.SpecialDay;
import com.thxpapa.juneberrydiary.dto.cal.CalRequestDto;
import com.thxpapa.juneberrydiary.dto.cal.CalResponseDto;
import com.thxpapa.juneberrydiary.repository.calRepository.SpecialDayRepository;
import com.thxpapa.juneberrydiary.util.SpecialDayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialDayServiceImpl implements SpecialDayService {

    private final SpecialDayUtil specialDayUtil;

    private final SpecialDayRepository specialDayRepository;

    @Override
    @CacheEvict(value="specialDay", allEntries = true)
    public void updateHoliday() { // holiday + anniversary(제헌절)
        final int startYear = 2004;
        final int endYear = LocalDate.now().plusYears(1).getYear();

        try {
            Long idx = 0L;
            LocalDate ld = null;
            String lastId = specialDayRepository.selectLastId();
            String holidayLastId = specialDayRepository.selectLastIdByDatStId("HOLIDAY");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            if (lastId!=null) {
                String[] buff = lastId.split(SpecialDay.idSplitter);
                String[] hlyBuff = null;

                if (holidayLastId!=null) {
                    hlyBuff = holidayLastId.split(SpecialDay.idSplitter);
                }

                if (buff.length > 1) {
                    if (hlyBuff!=null&&hlyBuff.length > 1) {
                        ld = LocalDate.parse(hlyBuff[0], formatter);
                    }
                    idx = Long.parseLong(buff[1]);
                }
            }

            for (int solYear = startYear; solYear <= endYear; solYear++) {
                if (ld != null && solYear < ld.getYear()) {
                    continue;
                }

                log.info(solYear + " year holiday data is being stored..");

                List<CalRequestDto.SpecialDayDto> specialDayList = specialDayUtil.getHoliDeInfo(String.valueOf(solYear));

                for (CalRequestDto.SpecialDayDto element: specialDayList) {
                    LocalDate date = LocalDate.parse(element.getLocdate(), formatter);

                    if (ld != null && !date.isAfter(ld)) {
                        continue;
                    }

                    Boolean isHoliday = element.getIsHoliday().equals("N") ? false : true;

                    specialDayRepository.save(SpecialDay.builder()
                            .specialDayUid(element.getLocdate()+SpecialDay.idSplitter+(++idx))
                            .datStId("HOLIDAY")
                            .dateName(element.getDateName())
                            .holidayCd(isHoliday)
                            .date(date)
                            .build());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @CacheEvict(value="specialDay", allEntries = true)
    public void updateAnniversary() {
        final int startYear = 2004;
        final int endYear = LocalDate.now().plusYears(1).getYear();
        final List<String> importantAnniversaryList = new ArrayList<>(
                Arrays.asList("식목일", "근로자의 날", "어버이 날", "스승의 날", "국군의 날", "한글날")
        );

        try {
            Long idx = 0L;
            LocalDate ld = null;
            String lastId = specialDayRepository.selectLastId();
            String anniversaryLastId = specialDayRepository.selectLastIdByDatStId("ANNIVERSARY");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            if (lastId!=null) {
                String[] buff = lastId.split(SpecialDay.idSplitter);
                String[] anyBuff = null;

                if (anniversaryLastId!=null) {
                    anyBuff = anniversaryLastId.split(SpecialDay.idSplitter);
                }

                if (buff.length > 1) {
                    if (anyBuff!=null&&anyBuff.length > 1) {
                        ld = LocalDate.parse(anyBuff[0], formatter);
                    }
                    idx = Long.parseLong(buff[1]);
                }
            }

            for (int solYear = startYear; solYear <= endYear; solYear++) {
                if (ld != null && solYear < ld.getYear()) {
                    continue;
                }

                log.info(solYear + " year anniversary data is being stored..");

                List<CalRequestDto.SpecialDayDto> anniversaryDayList = specialDayUtil.getAnniversaryInfo(String.valueOf(solYear));

                List<CalRequestDto.SpecialDayDto> annivList = anniversaryDayList.stream().filter(anniversaryDay -> {
                    return importantAnniversaryList.contains(anniversaryDay.getDateName());
                }).collect(Collectors.toList());

                for (CalRequestDto.SpecialDayDto element: annivList) {
                    LocalDate date = LocalDate.parse(element.getLocdate(), formatter);

                    if (ld != null && !date.isAfter(ld)) {
                        continue;
                    }

                    Boolean isHoliday = element.getIsHoliday().equals("N") ? false : true;

                    specialDayRepository.save(SpecialDay.builder()
                            .specialDayUid(element.getLocdate()+SpecialDay.idSplitter+(++idx))
                            .datStId("ANNIVERSARY")
                            .dateName(element.getDateName())
                            .holidayCd(isHoliday)
                            .date(date)
                            .build());
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @CacheEvict(value="specialDay", allEntries = true)
    public void update24Divisions() {
        final int startYear = 2004;
        final int endYear = LocalDate.now().plusYears(1).getYear();

        try {
            Long idx = 0L;
            LocalDate ld = null;
            String lastId = specialDayRepository.selectLastId();
            String divisionsLastId = specialDayRepository.selectLastIdByDatStId("24DIVISIONS");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            if (lastId!=null) {
                String[] buff = lastId.split(SpecialDay.idSplitter);
                String[] divBuff = null;

                if (divisionsLastId!=null) {
                    divBuff = divisionsLastId.split(SpecialDay.idSplitter);
                }

                if (buff.length > 1) {
                    if (divBuff!=null&&divBuff.length > 1) {
                        ld = LocalDate.parse(divBuff[0], formatter);
                    }
                    idx = Long.parseLong(buff[1]);
                }
            }

            for (int solYear = startYear; solYear <= endYear; solYear++) {
                if (ld != null && solYear < ld.getYear()) {
                    continue;
                }

                log.info(solYear + " year 24divisions data is being stored..");

                List<CalRequestDto.SpecialDayDto> specialDayList = specialDayUtil.get24DivisionsInfo(String.valueOf(solYear));

                for (CalRequestDto.SpecialDayDto element: specialDayList) {
                    LocalDate date = LocalDate.parse(element.getLocdate(), formatter);

                    if (ld != null && !date.isAfter(ld)) {
                        continue;
                    }

                    Boolean isHoliday = element.getIsHoliday().equals("N") ? false : true;

                    specialDayRepository.save(SpecialDay.builder()
                            .specialDayUid(element.getLocdate()+SpecialDay.idSplitter+(++idx))
                            .datStId("24DIVISIONS")
                            .dateName(element.getDateName())
                            .holidayCd(isHoliday)
                            .date(date)
                            .build());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "specialDay")
    public List<CalResponseDto.SpecialDayInfo> getSpecialDaysByMonth(LocalDate startDate, LocalDate endDate) {
        List<SpecialDay> specialDayList = specialDayRepository.findAllByDateBetween(startDate, endDate);
        return specialDayList.stream().map(specialDay -> {
                    return CalResponseDto.SpecialDayInfo.builder()
                            .datStId(specialDay.getDatStId())
                            .dateName(specialDay.getDateName())
                            .date(specialDay.getDate())
                            .holidayCd(specialDay.getHolidayCd())
                            .build();
                }).collect(Collectors.toList());
    }
}