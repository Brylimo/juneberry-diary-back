package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.cal.CalRequestDto;
import com.thxpapa.juneberrydiary.dto.cal.CalResponseDto;
import com.thxpapa.juneberrydiary.dto.cal.TagDto;
import com.thxpapa.juneberrydiary.service.cal.DayService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/cal")
public class CalController {
    private final TaskService taskService;
    private final DayService dayService;
    private final SpecialDayService specialDayService;
    private final ResponseDto responseDto;

    @GetMapping(value = "/getTagsByMonth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTagsByMonth(@RequestParam("year") String year, @RequestParam("month") String month) {
        try {
            LocalDate startDate = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month)).atDay(1);
            LocalDate endDate = startDate.plusMonths(1).minusDays(1);

            List<TagDto> tagList = new ArrayList<>();

            List<CalResponseDto.SpecialDayInfo> specialDayInfoList = specialDayService.getSpecialDaysByMonth(startDate, endDate);
            // List<Task> eventList = taskService.getEventsByMonth(startDate, endDate);

            // convert SpecialDay to TagDto
            tagList.addAll(specialDayInfoList.stream().map(specialDayInfo -> {
                if (specialDayInfo.getDatStId().equals("24DIVISIONS")) {
                    return TagDto.builder()
                            .date(specialDayInfo.getDate())
                            .name(specialDayInfo.getDateName())
                            .tagType("division")
                            .build();
                } else if (!specialDayInfo.getHolidayCd()) {
                    return TagDto.builder()
                            .date(specialDayInfo.getDate())
                            .name(specialDayInfo.getDateName())
                            .tagType("anniversary")
                            .build();
                }

                return TagDto.builder()
                        .date(specialDayInfo.getDate())
                        .name(specialDayInfo.getDateName())
                        .tagType("holiday")
                        .build();
            }).collect(Collectors.toList()));

            // convert Task to TagDto
            /*tagList.addAll(eventList.stream().map(event -> {
                return TagDto.builder()
                        .date(event.getDay().getDate())
                        .name(event.getContent())
                        .tagType("event")
                        .build();
            }).collect(Collectors.toList()));*/

            return responseDto.success(tagList);
        } catch (Exception e) {
            log.debug("getTagByMonth error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/addEventTagList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addEventTagList(@RequestBody CalRequestDto.SetEventTags calSetEventTagsRequestDto) {
        try {
            LocalDate thisDate = calSetEventTagsRequestDto.getDate();
            Optional<Day> optionalDay = dayService.findOneDay(thisDate);

           Day day = optionalDay.orElseGet(() -> dayService.createDay(thisDate).orElseThrow());
            day.setEventTagList(calSetEventTagsRequestDto.getEventTagList());

            return responseDto.success(day);
        } catch (Exception e) {
            log.debug("addEventTagList error occurred!");
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
