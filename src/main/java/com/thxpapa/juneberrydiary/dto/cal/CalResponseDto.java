package com.thxpapa.juneberrydiary.dto.cal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.thxpapa.juneberrydiary.enums.CheckStatus;
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
    public static class TodayTxtInfo {
        private LocalDate date;
        private String todayTxt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodoInfo {
        private Long id;
        private String date;
        private int position;
        private String content;
        private CheckStatus chkStatus;
        private int reward;
        private String color;
        private String groupName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagInfo {
        private LocalDate date;
        private String name;
        private String tagType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiInfo {
        private LocalDate date;
        private List<String> emojiCodeArray;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DayInfo {
        private Long dayUid;
        private LocalDate date;
        private String emojiCodes;
        private String todayTxt;
    }
}
