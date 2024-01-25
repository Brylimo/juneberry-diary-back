package com.thxpapa.juneberrydiary.dto.cal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class CalResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpecialDayInfo {
        private String datStId;
        private String dateName;
        private Boolean holidayCd;

        @JsonSerialize(using= LocalDateSerializer.class)
        @JsonDeserialize(using= LocalDateDeserializer.class)
        private LocalDate date;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventDayInfo {
        private LocalDate date;
        private List<String> eventTags;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodoInfo {
        private int position;
        private String content;
        private boolean doneCd;
        private int reward;
        private String color;
        private String groupName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagDto {
        private LocalDate date;
        private String name;
        private String tagType;
    }
}
