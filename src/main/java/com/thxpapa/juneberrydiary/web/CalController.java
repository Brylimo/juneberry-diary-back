package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.cal.SpecialDay;
import com.thxpapa.juneberrydiary.domain.cal.Task;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.cal.TagDto;
import com.thxpapa.juneberrydiary.service.cal.SpecialDayService;
import com.thxpapa.juneberrydiary.service.cal.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/cal")
public class CalController {
    private final TaskService taskService;
    private final SpecialDayService specialDayService;
    private final ResponseDto responseDto;

    @GetMapping(value = "/getTagsByMonth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTagsByMonth(@RequestParam("year") String year, @RequestParam("month") String month) {
        try {
            LocalDate startDate = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month)).atDay(1);
            LocalDate endDate = startDate.plusMonths(1).minusDays(1);

            List<TagDto> tagList = new ArrayList<>();

            List<SpecialDay> specialDayList = specialDayService.getSpecialDaysByMonth(startDate, endDate);
            List<Task> eventList = taskService.getEventsByMonth(startDate, endDate);

            // convert SpecialDay to TagDto
            tagList.addAll(specialDayList.stream().map(specialDay -> {
                if (specialDay.getDatStId().equals("24DIVISIONS")) {
                    return TagDto.builder()
                            .date(specialDay.getDate())
                            .name(specialDay.getDateName())
                            .tagType("division")
                            .build();
                } else if (!specialDay.getHolidayCd()) {
                    return TagDto.builder()
                            .date(specialDay.getDate())
                            .name(specialDay.getDateName())
                            .tagType("anniversary")
                            .build();
                }

                return TagDto.builder()
                        .date(specialDay.getDate())
                        .name(specialDay.getDateName())
                        .tagType("holiday")
                        .build();
            }).collect(Collectors.toList()));

            // convert Task to TagDto
            tagList.addAll(eventList.stream().map(event -> {
                return TagDto.builder()
                        .date(event.getDay().getDate())
                        .name(event.getContent())
                        .tagType("event")
                        .build();
            }).collect(Collectors.toList()));

            return responseDto.success(tagList);
        } catch (Exception e) {
            log.debug("getTagByMonth error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/calendar")
    public String main(Model model) {
        List<String> week = new ArrayList<>(
                Arrays.asList("Sun", "Mon", "Thu", "Wed", "Thurs", "Fri", "Sat")
        );

        model.addAttribute("week", week);

        return "score/calendar";
    }
}
