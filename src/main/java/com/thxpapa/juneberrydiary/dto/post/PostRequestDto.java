package com.thxpapa.juneberrydiary.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class PostRequestDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WritePost {
        private String postId;
        private String date;
        private String title;
        private String description;
        private String content;
        private String thumbnailPath;
        private Boolean isTemp;
        private Boolean isPublic;
        private MultipartFile thumbnailImg;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostDto {
        private String postId;
        private String date;
        private String title;
        private String content;
        private Boolean isTemp;
    }
}
