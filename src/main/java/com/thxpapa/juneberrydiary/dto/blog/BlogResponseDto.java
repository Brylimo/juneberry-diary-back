package com.thxpapa.juneberrydiary.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BlogResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BlogInfo {
        private String blogId;
        private String blogName;
    }
}
