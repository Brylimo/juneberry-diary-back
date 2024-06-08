package com.thxpapa.juneberrydiary.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class PostResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class imageInfo {
        private String imagePath;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class postInfo {
        private String id;
        private LocalDate date;
        private String title;
        private String content;
        private Boolean isTemp;
    }
}
