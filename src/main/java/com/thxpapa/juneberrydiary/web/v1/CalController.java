package com.thxpapa.juneberrydiary.web.v1;

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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/cal")
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

            List<CalResponseDto.TagInfo> tagList = new ArrayList<>();
            List<CalResponseDto.SpecialDayInfo> specialDayInfoList = specialDayService.getSpecialDaysByMonth(startDate, endDate);

            // convert SpecialDay to TagInfo
            tagList.addAll(specialDayInfoList.stream().map(specialDayInfo -> {
                if (specialDayInfo.getDatStId().equals("24DIVISIONS")) {
                    return CalResponseDto.TagInfo.builder()
                            .date(specialDayInfo.getDate())
                            .name(specialDayInfo.getDateName())
                            .tagType("division")
                            .build();
                } else if (!specialDayInfo.getHolidayCd()) {
                    return CalResponseDto.TagInfo.builder()
                            .date(specialDayInfo.getDate())
                            .name(specialDayInfo.getDateName())
                            .tagType("anniversary")
                            .build();
                }

                return CalResponseDto.TagInfo.builder()
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

    @GetMapping(value = "/getEmojisByMonth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEmojisByMonth(@RequestParam("year") String year, @RequestParam("month") String month, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        try {
            LocalDate startDate = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month)).atDay(1);
            LocalDate endDate = startDate.plusMonths(1).minusDays(1);

            List<CalResponseDto.EmojiInfo> emojiInfoList = dayService.findEmojiByMonth(juneberryUser, startDate, endDate);
            return responseDto.success(emojiInfoList);
        } catch (Exception e) {
            log.debug("getEmojisByMonth error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/addEventTagList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addEventTagList(@RequestBody CalRequestDto.SetEventTags calSetEventTagsRequestDto, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(calSetEventTagsRequestDto.getDate(), formatter);
            List<String> eventTagList = calSetEventTagsRequestDto.getEventTagList();

            dayService.storeEventTagList(juneberryUser, date, eventTagList);
            return responseDto.success();
        } catch (Exception e) {
            log.debug("addEventTagList error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/addDayEmoji", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDayEmoji(@RequestBody CalRequestDto.DayEmoji calDayEmojiRequestDto, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(calDayEmojiRequestDto.getDate(), formatter);
            List<String> emojiCodeList = calDayEmojiRequestDto.getEmojiCodeArray();

            dayService.storeDayEmoji(juneberryUser, date, emojiCodeList);
            return responseDto.success();
        } catch (Exception e) {
            log.debug("addDayEmoji error occurred!");
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

    @GetMapping(value = "/getTodayTxt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTodayTxt(@RequestParam("date") String date, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate targetDate = LocalDate.parse(date, formatter);
            Optional<String> todoTxt = dayService.findTodayTxt(juneberryUser, targetDate);

            CalResponseDto.TodayTxtInfo todayTxtObj = CalResponseDto.TodayTxtInfo.builder()
                    .todayTxt(todoTxt.orElse(""))
                    .date(targetDate)
                    .build();
            return responseDto.success(todayTxtObj);
        } catch(NoSuchElementException e) {
            return responseDto.success();
        } catch (Exception e) {
            log.debug("getTodayTxt error occurred!");
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
                        .chkStatus(todo.get().getChkStatus())
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

            return responseDto.success(CalResponseDto.DayInfo.builder()
                            .dayUid(day.getDayUid())
                            .date(day.getDate())
                            .emojiCodes(day.getEmojiCodes())
                            .todayTxt(day.getTodayTxt())
                            .build());
        } catch (Exception e) {
            log.debug("updateTodayTxt error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/updateTodoChk", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTodoChk(@RequestBody CalRequestDto.TodoChk calTodoChkDto, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(calTodoChkDto.getDate(), formatter);
            Optional<Todo> todo = todoService.updateTodoChk(juneberryUser, date, calTodoChkDto);

            if (todo.isEmpty()) {
                return responseDto.success("todo가 비어있습니다.");
            } else {
                return responseDto.success(CalResponseDto.TodoInfo.builder()
                        .date(date.toString())
                        .position(todo.get().getPosition())
                        .content(todo.get().getContent())
                        .chkStatus(todo.get().getChkStatus())
                        .reward(todo.get().getReward())
                        .color(todo.get().getTodoGroup().getColor())
                        .groupName(todo.get().getTodoGroup().getName())
                        .build());
            }
        } catch (Exception e) {
            log.debug("updateTodoChk error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
