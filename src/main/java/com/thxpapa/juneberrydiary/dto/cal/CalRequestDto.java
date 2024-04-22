package com.thxpapa.juneberrydiary.dto.cal;

import lombok.*;
import java.util.List;

public class CalRequestDto {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetEventTags {
        private String date;
        private List<String> eventTagList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpecialDayDto {
        private String dateKind;
        private String dateName;
        private String isHoliday;
        private String kst;
        private String locdate;
        private String remarks;
        private String seq;
        private String sunLongitude;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodoLine {
        private String date;
        private String groupName;
        private String content;
        private int position;
        private boolean doneCd;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodayTxt {
        private String date;
        private String todayTxt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodoChk {
        private String date;
        private int position;
        private String check;
    }
}
