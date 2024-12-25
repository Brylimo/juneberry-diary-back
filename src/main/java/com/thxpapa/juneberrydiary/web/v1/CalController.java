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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "캘린더 일반 태그 목록 조회", description = "캘린더 렌더링에 필요한 모든 태그들을 조회합니다.")
    @GetMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTagsByMonth(
            @Parameter(description = "년")
            @RequestParam("year") String year,
            @Parameter(description = "월")
            @RequestParam("month") String month) {
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

    @Operation(summary = "캘린더 이벤트 태그 목록 조회", description = "사용자가 생성한 모든 캘린더 이벤트 태그들을 조회합니다.")
    @GetMapping(value = "/event-tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEventTagsByMonth(
            @Parameter(description = "년")
            @RequestParam("year") String year,
            @Parameter(description = "월")
            @RequestParam("month") String month,
            @AuthenticationPrincipal JuneberryUser juneberryUser) {
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

    @Operation(summary = "캘린더 이모지 목록 조회", description = "사용자가 생성한 모든 이모지들을 조회합니다.")
    @GetMapping(value = "/emojis", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEmojisByMonth(
            @Parameter(description = "년")
            @RequestParam("year") String year,
            @Parameter(description = "월")
            @RequestParam("month") String month,
            @AuthenticationPrincipal JuneberryUser juneberryUser) {
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

    @Operation(summary = "이벤트 태그 저장", description = "해당 일자에 속하는 모든 이벤트 태그를 받아 저장합니다.")
    @PostMapping(value = "/event-tags", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(summary = "이모지 저장", description = "해당 일자에 속하는 이모지를 저장합니다.")
    @PostMapping(value = "/emoji", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(summary = "투두 목록 조회", description = "해당 일자에 속하는 모든 투두 목록을 조회합니다.")
    @GetMapping(value = "/todos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTodosByDay(
            @Parameter(description = "날짜")
            @RequestParam("date") String date,
            @AuthenticationPrincipal JuneberryUser juneberryUser) {
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

    @Operation(summary = "오늘의 한마디 조회", description = "해당 일자에 속하는 오늘의 한마디를 조회합니다.")
    @GetMapping(value = "/today-text", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTodayTxt(
            @Parameter(description = "날짜")
            @RequestParam("date") String date,
            @AuthenticationPrincipal JuneberryUser juneberryUser) {
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

    @Operation(summary = "투두 저장", description = "해당 일자에 속하는 투두를 저장합니다.")
    @PostMapping(value = "/todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addOneTodo(@RequestBody CalRequestDto.TodoLine calTodoLineRequestDto, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(calTodoLineRequestDto.getDate(), formatter);
            Optional<Todo> todo = todoService.createTodoByTodoLine(juneberryUser, date, calTodoLineRequestDto);
            if (todo.isEmpty()) {
                return responseDto.success("todo가 비어있습니다.");
            } else {
                return responseDto.success(CalResponseDto.TodoInfo.builder()
                        .id(todo.get().getTodoUid())
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

    @Operation(summary = "오늘의 한마디 업데이트", description = "해당 일자에 속하는 오늘의 한마디를 업데이트합니다.")
    @PutMapping(value = "/today-text", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(summary = "투두 업데이트", description = "투두를 업데이트합니다.")
    @PatchMapping(value = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTodo(
            @Parameter(description = "투두의 고유 아이디")
            @PathVariable("id") String id,
            @RequestBody CalRequestDto.TodoLine calTodoLineRequestDto) {

        try {
            Long todoId = Long.parseLong(id);
            Optional<Todo> todo = todoService.updateTodo(todoId, calTodoLineRequestDto);

            if (todo.isEmpty()) {
                return responseDto.success("todo가 비어있습니다.");
            } else {
                return responseDto.success(CalResponseDto.TodoInfo.builder()
                        .id(todo.get().getTodoUid())
                        .date(todo.get().getDay().toString())
                        .position(todo.get().getPosition())
                        .content(todo.get().getContent())
                        .chkStatus(todo.get().getChkStatus())
                        .reward(todo.get().getReward())
                        .color(todo.get().getTodoGroup().getColor())
                        .groupName(todo.get().getTodoGroup().getName())
                        .build());
            }
        } catch (NumberFormatException e) {
            return responseDto.fail("todo id 값은 숫자여야 합니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.debug("updateTodo error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
