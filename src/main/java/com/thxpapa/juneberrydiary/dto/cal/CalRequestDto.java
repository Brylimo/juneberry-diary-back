package com.thxpapa.juneberrydiary.dto.cal;

import lombok.*;

import java.time.LocalDate;
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
}
