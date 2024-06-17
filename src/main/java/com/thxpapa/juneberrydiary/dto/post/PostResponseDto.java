package com.thxpapa.juneberrydiary.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PostResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageInfo {
        private String imagePath;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostInfo {
        private String id;
        private LocalDateTime updatedDateTime;
        private String title;
        private String content;
        private Boolean isTemp;
    }
}
