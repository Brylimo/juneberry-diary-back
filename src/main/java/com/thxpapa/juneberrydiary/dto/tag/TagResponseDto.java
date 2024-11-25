package com.thxpapa.juneberrydiary.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TagResponseDto {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TagInfo {
        private String name;
    }
}
