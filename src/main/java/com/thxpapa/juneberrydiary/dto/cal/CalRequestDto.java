package com.thxpapa.juneberrydiary.dto.cal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CalRequestDto {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class EventTagInfo {
        private String eventText;
    }
}
