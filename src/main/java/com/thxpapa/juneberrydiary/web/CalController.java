package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.cal.Day;
import com.thxpapa.juneberrydiary.domain.cal.Todo;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.cal.CalRequestDto;
import com.thxpapa.juneberrydiary.dto.cal.CalResponseDto;
import com.thxpapa.juneberrydiary.service.cal.DayService;
import com.thxpapa.juneberrydiary.service.cal.SpecialDayService;
import com.thxpapa.juneberrydiary.service.cal.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/cal")
public class CalController {
    private final TodoService todoService;
    private final DayService dayService;
    private final SpecialDayService specialDayService;
    private final ResponseDto responseDto;

    @GetMapping(value = "/getTagsByMonth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTagsByMonth(@RequestParam("year") String year, @RequestParam("month") String month) {
        try {
            LocalDate startDate = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month)).atDay(1);
            LocalDate endDate = startDate.plusMonths(1).minusDays(1);

            List<CalResponseDto.TagDto> tagList = new ArrayList<>();
            List<CalResponseDto.SpecialDayInfo> specialDayInfoList = specialDayService.getSpecialDaysByMonth(startDate, endDate);

            // convert SpecialDay to TagDto
            tagList.addAll(specialDayInfoList.stream().map(specialDayInfo -> {
                if (specialDayInfo.getDatStId().equals("24DIVISIONS")) {
                    return CalResponseDto.TagDto.builder()
                            .date(specialDayInfo.getDate())
                            .name(specialDayInfo.getDateName())
                            .tagType("division")
                            .build();
                } else if (!specialDayInfo.getHolidayCd()) {
                    return CalResponseDto.TagDto.builder()
                            .date(specialDayInfo.getDate())
                            .name(specialDayInfo.getDateName())
                            .tagType("anniversary")
                            .build();
                }

                return CalResponseDto.TagDto.builder()
                        .date(specialDayInfo.getDate())
                        .name(specialDayInfo.getDateName())
                        .tagType("holiday")
                        .build();
            }).collect(Collectors.toList()));

            return responseDto.success(tagList);
        } catch (Exception e) {
            log.debug("getTagByMonth error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getEventTagsByMonth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEventTagsByMonth(@RequestParam("year") String year, @RequestParam("month") String month, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        try {
            LocalDate startDate = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month)).atDay(1);
            LocalDate endDate = startDate.plusMonths(1).minusDays(1);

            List<CalResponseDto.EventDayInfo> eventDayInfoList = dayService.findEventDayByMonth(juneberryUser, startDate, endDate);
            return responseDto.success(eventDayInfoList);
        } catch (Exception e) {
            log.debug("getEventTagsByMonth error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/addEventTagList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addEventTagList(@RequestBody CalRequestDto.SetEventTags calSetEventTagsRequestDto, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(calSetEventTagsRequestDto.getDate(), formatter);
            List<String> eventTagList = calSetEventTagsRequestDto.getEventTagList();

            Day day = dayService.storeEventTagList(juneberryUser, date, eventTagList);
            return responseDto.success(day);
        } catch (Exception e) {
            log.debug("addEventTagList error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getTodosByDay", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTodosByDay(@RequestParam("date") String date, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate targetDate = LocalDate.parse(date, formatter);
            List<CalResponseDto.TodoInfo> todoList = todoService.getTodosByDate(juneberryUser, targetDate);

            return responseDto.success(todoList);
        } catch(NoSuchElementException e) {
            return responseDto.success();
        } catch (Exception e) {
            log.debug("getTodosByDay error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/addOneTodo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addOneTodo(@RequestBody CalRequestDto.TodoLine calTodoLineRequestDto, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(calTodoLineRequestDto.getDate(), formatter);
            Optional<Todo> todo = todoService.createTodoByTodoLine(juneberryUser, date, calTodoLineRequestDto);
            if (todo.isEmpty()) {
                return responseDto.success("todo가 비어있습니다.");
            } else {
                return responseDto.success(CalResponseDto.TodoInfo.builder()
                        .date(date.toString())
                        .position(todo.get().getPosition())
                        .content(todo.get().getContent())
                        .doneCd(todo.get().isDoneCd())
                        .reward(todo.get().getReward())
                        .color(todo.get().getTodoGroup().getColor())
                        .groupName(todo.get().getTodoGroup().getName())
                        .build());
            }
        } catch (Exception e) {
            log.debug("addOneTodo error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/updateTodayTxt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTodayTxt(@RequestBody CalRequestDto.TodayTxt calTodayTxtRequestDto, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(calTodayTxtRequestDto.getDate(), formatter);
            String todayTxt = calTodayTxtRequestDto.getTodayTxt();

            Day day = dayService.storeTodayTxt(juneberryUser, date, todayTxt);

            return responseDto.success(day);
        } catch (Exception e) {
            log.debug("updateTodayTxt error occurred!");
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
